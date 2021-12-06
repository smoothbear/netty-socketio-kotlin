package io.smoothbear.socketio.transport

import io.smoothbear.socketio.*
import io.smoothbear.socketio.handler.ClientHead
import io.smoothbear.socketio.namespace.Namespace
import io.smoothbear.socketio.protocol.Packet
import io.smoothbear.socketio.protocol.PacketType
import org.slf4j.LoggerFactory
import java.net.SocketAddress
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class NamespaceClient (
    val baseClient: ClientHead,
    val namespace: Namespace
) : SocketIOClient {

    init {
        namespace.addClient(this)
    }

    private val disconnected: AtomicBoolean = AtomicBoolean()

    companion object {
        @JvmField
        val logger = LoggerFactory.getLogger(NamespaceClient::class.java)
    }

    fun onDisconnect() {
        disconnected.set(true)

        baseClient.removeNamespaceClient(this)
        namespace.onDisconnect(this)
    }

    fun isConnected(): Boolean = !disconnected.get() && baseClient.isConnected()

    override fun getTransport(): Transport = baseClient.currentTransport
    override fun isChannelOpen(): Boolean = baseClient.isChannelOpen()
    override fun getNamespace(): SocketIONamespace = namespace

    override fun sendEvent(name: String, vararg data: Any) {
        val packet = Packet(PacketType.MESSAGE)
        packet.subType = PacketType.EVENT
        packet.name = name
        packet.data = listOf(data)
        send(packet)
    }

    override fun sendEvent(name: String, ackCallback: AckCallback<*>, vararg data: Any) {
        val packet = Packet(PacketType.MESSAGE)
        packet.subType = PacketType.EVENT
        packet.name = name
        packet.data = listOf(data)
        send(packet)
    }

    override fun send(packet: Packet, ackCallback: AckCallback<*>) {
        if (!isConnected()) {
            ackCallback.onTimeout()
            return
        }

        val index: Long = baseClient.ackManager.registerAck(getSessionId(), ackCallback)
        packet.ackId = index
        send(packet)
    }

    override fun send(packet: Packet) {
        if (!isConnected())
            return

        baseClient.send(packet.withNsp(namespace.getName()))
    }

    override fun disconnect() {
        disconnected.set(true)

        baseClient.removeNamespaceClient(this)
        namespace.onDisconnect(this)

        logger.debug("Client ${baseClient.sessionId} for namespace ${getNamespace().getName()} has been disconnected")
    }

    override fun getSessionId(): UUID = baseClient.sessionId
    override fun getRemoteAddress(): SocketAddress = baseClient.getRemoteAddress()

    override fun joinRoom(room: String) {
        namespace.joinRoom(room, getSessionId())
    }

    override fun leaveRoom(room: String) {
        namespace.leaveRoom(room, getSessionId())
    }

    override fun set(key: String, obj: Any) {
        baseClient.store.set(key, obj)
    }

    override fun <T> get(key: String): T = baseClient.store.get(key)
    override fun has(key: String): Boolean = baseClient.store.has(key)
    override fun del(key: String): Unit = baseClient.store.del(key)
    override fun getAllRooms(): Set<String> = namespace.getRooms(this)
    override fun getHandShakeData(): HandShakeData = baseClient.handshakeData

}