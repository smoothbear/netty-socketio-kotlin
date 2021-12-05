package io.smoothbear.socketio

import io.smoothbear.socketio.protocol.Packet
import io.smoothbear.socketio.store.Store
import java.net.SocketAddress
import java.util.*

interface SocketIOClient : ClientOperations, Store {

    fun getHandShakeData(): HandShakeData

    fun getTransport(): Transport

    fun sendEvent(name: String, ackCallback: AckCallback<*>)

    fun send(packet: Packet, ackCallback: AckCallback<*>)

    fun getNamespace(): SocketIONamespace

    fun getSessionId(): UUID

    fun getRemoteAddress(): SocketAddress

    fun isChannelOpen(): Boolean

    fun joinRoom(room: String)

    fun leaveRoom(room: String)

    fun getAllRooms(): Set<String>
}