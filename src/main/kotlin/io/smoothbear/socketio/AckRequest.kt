package io.smoothbear.socketio

import io.smoothbear.socketio.protocol.Packet
import io.smoothbear.socketio.protocol.PacketType
import java.util.concurrent.atomic.AtomicBoolean

class AckRequest (
    private val originalPacket: Packet,
    private val client: SocketIOClient
) {

    private val sent: AtomicBoolean = AtomicBoolean()

    fun isAckRequested(): Boolean = originalPacket.isAckRequested()

    fun sendAckData(vararg objs: Any) {
        val args = objs.toList()
        sendAckData(args)
    }

    fun sendAckData(objs: List<Any>) {
        if (!isAckRequested() || !sent.compareAndSet(false, true))
            return

        val ackPacket = Packet(PacketType.MESSAGE)
        ackPacket.subType = PacketType.ACK
        ackPacket.ackId = originalPacket.ackId
        ackPacket.data = objs
        client.send(ackPacket)
    }
}