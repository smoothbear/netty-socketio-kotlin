package io.smoothbear.socketio.transport

import io.netty.channel.ChannelInboundHandlerAdapter

class WebSocketTransport : ChannelInboundHandlerAdapter() {
    companion object {
        const val NAME = "websocket"
    }
}