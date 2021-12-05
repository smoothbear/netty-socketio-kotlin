package io.smoothbear.socketio.transport

import io.netty.channel.ChannelInboundHandlerAdapter

class PollingTransport : ChannelInboundHandlerAdapter() {
    companion object {
        const val NAME = "polling"
    }
}