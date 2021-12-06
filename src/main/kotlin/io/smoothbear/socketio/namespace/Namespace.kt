package io.smoothbear.socketio.namespace

import io.netty.util.internal.PlatformDependent
import io.smoothbear.socketio.*
import io.smoothbear.socketio.annotation.ScannerEngine
import io.smoothbear.socketio.listener.*
import io.smoothbear.socketio.protocol.JsonSupport
import io.smoothbear.socketio.protocol.Packet
import io.smoothbear.socketio.store.StoreFactory
import io.smoothbear.socketio.store.pubsub.JoinLeaveMessage
import io.smoothbear.socketio.store.pubsub.PubSubType
import io.smoothbear.socketio.transport.NamespaceClient
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KClass

class Namespace (
    private val name: String,
    private val ackMode: AckMode,
    private val storeFactory: StoreFactory,
    private val exceptionListener: ExceptionListener,
    private val jsonSupport: JsonSupport
) : SocketIONamespace {

//    public constructor(name: String, configuration: Configuration) {
//        this.
//    }

    companion object {
        const val DEFAULT_NAME = ""
    }

    private val engine: ScannerEngine = ScannerEngine()
    private val eventListeners: ConcurrentMap<String, EventEntry<Any?>> = PlatformDependent.newConcurrentHashMap()
    private val connectListeners: Queue<ConnectListener> = ConcurrentLinkedQueue()
    private val disconnectListeners: Queue<DisconnectListener> = ConcurrentLinkedQueue()
    private val pingListeners: Queue<PingListener> = ConcurrentLinkedQueue()
    private val eventInterceptors: Queue<EventInterceptor> = ConcurrentLinkedQueue()

    private val allClients: MutableMap<UUID, SocketIOClient> = PlatformDependent.newConcurrentHashMap()
    private val roomClients: ConcurrentMap<String, MutableSet<UUID>> = PlatformDependent.newConcurrentHashMap()
    private val clientRooms: ConcurrentMap<UUID, MutableSet<String>> = PlatformDependent.newConcurrentHashMap()

    fun addClient(client: SocketIOClient) {
        allClients[client.getSessionId()] = client
    }

    fun onEvent(client: NamespaceClient, eventName: String, args: List<Any>, ackRequest: AckRequest) {
        val entry = eventListeners[eventName] ?: return

        try {
            val listeners: Queue<DataListener<Any?>> = entry.getListeners()
            for (dataListener in listeners) {
                val data = getEventData(args, dataListener)
                dataListener.onData(client, data, ackRequest)
            }

            for (eventInterceptor in eventInterceptors) {
                eventInterceptor.onEvent(client, eventName, args, ackRequest)
            }
        } catch (e: Exception) {
            exceptionListener.onEventException(e, args, client)

            if (ackMode == AckMode.AUTO_SUCCESS_ONLY)
                return
        }

        sendAck(ackRequest)
    }

    fun sendAck(ackRequest: AckRequest) {
        if (ackMode == AckMode.AUTO || ackMode == AckMode.AUTO_SUCCESS_ONLY) {
            ackRequest.sendAckData(emptyList())
        }
    }

    fun getEventData(args: List<Any>, dataListener: DataListener<*>): Any? {
        if (dataListener is MultiTypeEventListener) {
            return MultiTypeArgs(args)
        } else {
            if (args.isNotEmpty())
                return args[0]
        }

        return null
    }

    fun onDisconnect(client: SocketIOClient) {
        val joinedRooms = client.getAllRooms()
        allClients.remove(client.getSessionId())

        for (joinedRoom in joinedRooms) {
            leave(roomClients, joinedRoom, client.getSessionId())
            storeFactory.pubSubStore().publish(
                PubSubType.LEAVE, JoinLeaveMessage(client.getSessionId(), joinedRoom, name)
            )
        }
        clientRooms.remove(client.getSessionId())

        try {
            for (listener in disconnectListeners) {
                listener.onDisconnect(client)
            }
        } catch (e: Exception) {
            exceptionListener.onDisconnectException(e, client)
        }
    }

    fun onConnect(client: SocketIOClient) {
        join(name, client.getSessionId())
        storeFactory.pubSubStore().publish(
            PubSubType.JOIN, JoinLeaveMessage(client.getSessionId(), name, name)
        )

        try {
            for (listener in connectListeners) {
                listener.onConnect(client)
            }
        } catch (e: Exception) {
            exceptionListener.onConnectException(e, client)
        }
    }

    fun onPing(client: SocketIOClient) {
        try {
            for (listener in pingListeners) {
                listener.onPing(client)
            }
        } catch (e: Exception) {
            exceptionListener.onPingException(e, client)
        }
    }

    fun joinRoom(room: String, sessionId: UUID) {
        join(room, sessionId)
        storeFactory.pubSubStore().publish(PubSubType.JOIN, JoinLeaveMessage(sessionId, room, name))
    }

    fun dispatch(room: String, packet: Packet) {
        val clients = getRoomClients(room)

        for (client in clients) {
            client.send(packet)
        }
    }

    fun <K, V> join(map: ConcurrentMap<K, MutableSet<V>>, key: K, value: V) {
        var clients = map[key]
        if (clients == null) {
            clients = Collections.newSetFromMap(PlatformDependent.newConcurrentHashMap())
            val oldClients = map.putIfAbsent(key, clients)
            if (oldClients != null)
                clients = oldClients
        }
        clients!! += value

        if (clients != map[key]) {
            join(map, key, value)
        }
    }

    fun join(room: String, sessionId: UUID) {
        join(roomClients, room, sessionId)
        join(clientRooms, sessionId, room)
    }

    fun leaveRoom(room: String, sessionId: UUID) {
        leave(room, sessionId)
        storeFactory.pubSubStore().publish(PubSubType.LEAVE, JoinLeaveMessage(sessionId, room, name))
    }

    fun <K, V> leave(map: ConcurrentMap<K, MutableSet<V>>, room: K, sessionId: V) {
        val clients = map[room] ?: return

        clients.remove(sessionId)

        if (clients.isEmpty())
            map.remove(room, Collections.emptySet())
    }

    fun leave(room: String, sessionId: UUID) {
        leave(roomClients, room, sessionId)
        leave(clientRooms, sessionId, room)
    }

    fun getRooms(client: SocketIOClient): Set<String> {
        val res = clientRooms[client.getSessionId()] ?: return emptySet()

        return Collections.unmodifiableSet(res)
    }

    fun getRooms(): Set<String> = roomClients.keys

    fun getRoomClients(room: String): Iterable<SocketIOClient> {
        val sessionIds: MutableSet<UUID> = roomClients[room] ?: Collections.emptySet()

        val result = arrayListOf<SocketIOClient>()
        for (sessionId in sessionIds) {
            val client = allClients[sessionId]

            if (client != null)
                result.add(client)
        }

        return result
    }

    override fun getName(): String = name

    override fun <T> addEventListener(eventName: String, eventClass: KClass<*>, listener: DataListener<Any?>) {
        var entry = eventListeners[eventName]
        if (entry == null) {
            entry = EventEntry()
            val oldEntry = eventListeners.putIfAbsent(eventName, entry)
            if (oldEntry != null) {
                entry = oldEntry
            }
        }

        entry.addListener(listener)
    }

    override fun addEventInterceptor(eventInterceptor: EventInterceptor) {
        eventInterceptors.add(eventInterceptor)
    }

    @Deprecated("Not supported this version.")
    override fun addMultiTypeEventListener(
        eventName: String,
        listener: MultiTypeEventListener,
        vararg eventClass: KClass<*>
    ) {}

    override fun addPingListener(listener: PingListener) {
        pingListeners.add(listener)
    }

    override fun getBroadcastOperations(): BroadcastOperations {
        return SingleRoomBroadcastOperations(name, name, allClients.values, storeFactory)
    }

    override fun getRoomOperations(room: String): BroadcastOperations {
        return SingleRoomBroadcastOperations(name, room, getRoomClients(room), storeFactory)
    }

    override fun addConnectListener(listener: ConnectListener) {
        connectListeners.add(listener)
    }

    override fun addDisconnectListener(listener: DisconnectListener) {
        disconnectListeners.add(listener)
    }

    override fun removeAllListeners(eventName: String) {
        val entry = eventListeners.remove(eventName)
        if (entry != null)
            jsonSupport.removeEventMapping(name, eventName)
    }

    override fun addListeners(listeners: Any) {
        addListeners(listeners, listeners::class)
    }

    override fun addListeners(listeners: Any, listenersClass: KClass<*>) {}

    override fun getAllClients(): Collection<SocketIOClient> = Collections.unmodifiableCollection(allClients.values)

    override fun getClient(uuid: UUID): SocketIOClient? = allClients[uuid]

}