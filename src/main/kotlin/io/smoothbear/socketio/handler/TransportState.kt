package io.smoothbear.socketio.handler

import io.netty.channel.Channel
import io.smoothbear.socketio.protocol.Packet
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class TransportState {

    var packetsQueue = ConcurrentLinkedQueue<Packet>()
    var channel: Channel? = null

    fun setPacketsQueue(packetsQueue: Queue<Packet>) {
        this.packetsQueue = packetsQueue as ConcurrentLinkedQueue<Packet>
    }

    fun update(channel: Channel?): Channel? {
        val prevChannel = this.channel
        this.channel = channel

        return prevChannel
    }
}