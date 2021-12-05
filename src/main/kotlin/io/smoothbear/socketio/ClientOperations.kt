package io.smoothbear.socketio

import io.smoothbear.socketio.protocol.Packet

interface ClientOperations {
    fun send(packet: Packet)
    fun disconnect()
    fun sendEvent(name: String, vararg data: Any)
}