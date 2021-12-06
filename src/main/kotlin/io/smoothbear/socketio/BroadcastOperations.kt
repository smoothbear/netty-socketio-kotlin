package io.smoothbear.socketio

import io.smoothbear.socketio.protocol.Packet

interface BroadcastOperations : ClientOperations {

    fun getClients(): Collection<SocketIOClient>

    fun <T> send(packet: Packet, ackCallback: BroadcastAckCallback<T>)

    fun sendEvent(name: String, excludedClient: SocketIOClient, vararg data: Any)

    fun <T> sendEvent(name: String, data: Any, ackCallback: BroadcastAckCallback<T>)

    fun <T> sendEvent(name: String, data: Any, excludedClient: SocketIOClient, ackCallback: BroadcastAckCallback<T>)

}