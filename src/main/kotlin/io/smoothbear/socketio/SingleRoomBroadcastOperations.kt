package io.smoothbear.socketio

import io.smoothbear.socketio.misc.IterableCollection
import io.smoothbear.socketio.protocol.Packet
import io.smoothbear.socketio.protocol.PacketType
import io.smoothbear.socketio.store.StoreFactory
import io.smoothbear.socketio.store.pubsub.DispatchMessage
import io.smoothbear.socketio.store.pubsub.PubSubType
import java.util.*

class SingleRoomBroadcastOperations (
    val namespace: String,
    val room: String,
    val clients: Iterable<SocketIOClient>,
    val storeFactory: StoreFactory
) : BroadcastOperations {

    fun dispatch(packet: Packet) {
        this.storeFactory.pubSubStore().publish(
            PubSubType.DISPATCH,
            DispatchMessage(this.room, packet, this.namespace)
        )
    }

    override fun getClients(): Collection<SocketIOClient> {
        return IterableCollection(clients)
    }

    override fun send(packet: Packet) {
        for (client in clients) {
            client.send(packet)
        }
        dispatch(packet)
    }

    override fun <T> send(packet: Packet, ackCallback: BroadcastAckCallback<T>) {
        for (client in clients) {
            client.send(packet, ackCallback.createClientCallback(client))
        }

        ackCallback.loopFinished()
    }

    override fun disconnect() {
        for (client in clients) {
            client.disconnect()
        }
    }

    override fun sendEvent(name: String, excludedClient: SocketIOClient, vararg data: Any) {
        val packet = Packet(PacketType.MESSAGE)
        packet.subType = PacketType.EVENT
        packet.name = name
        packet.data = listOf(data)

        for (client in clients) {
            if (client.getSessionId().equals(excludedClient.getSessionId()))
                continue
            client.send(packet)
        }

        dispatch(packet)
    }

    override fun sendEvent(name: String, vararg data: Any) {
        val packet = Packet(PacketType.MESSAGE)
        packet.subType = PacketType.EVENT
        packet.name = name
        packet.data = listOf(data)

        send(packet)
    }

    override fun <T> sendEvent(name: String, data: Any, ackCallback: BroadcastAckCallback<T>) {
        for (client in clients) {
            client.sendEvent(name, ackCallback.createClientCallback(client), data)
        }

        ackCallback.loopFinished()
    }

    override fun <T> sendEvent(
        name: String,
        data: Any,
        excludedClient: SocketIOClient,
        ackCallback: BroadcastAckCallback<T>
    ) {
        for (client in clients) {
            if (client.getSessionId() == excludedClient.getSessionId())
                continue

            client.sendEvent(name, ackCallback.createClientCallback(client), data)
        }

        ackCallback.loopFinished()
    }
}