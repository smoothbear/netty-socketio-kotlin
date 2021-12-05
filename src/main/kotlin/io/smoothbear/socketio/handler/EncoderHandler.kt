package io.smoothbear.socketio.handler

import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOutboundHandler
import io.netty.channel.ChannelPromise
import io.netty.util.AttributeKey
import org.slf4j.LoggerFactory
import java.net.SocketAddress

@Sharable
class EncoderHandler : ChannelOutboundHandler {

    companion object {
        @JvmField
        val ORIGIN = AttributeKey.valueOf<String>("origin")

        @JvmField
        val USER_AGENT = AttributeKey.valueOf<String>("userAgent")

        @JvmField
        val B64 = AttributeKey.valueOf<Boolean>("b64")

        @JvmField
        val JSONP_INDEX = AttributeKey.valueOf<Int>("jsonpIndex")

        @JvmField
        val WRITE_ONCE = AttributeKey.valueOf<Boolean>("writeOnce")

        @JvmField
        val logger = LoggerFactory.getLogger(EncoderHandler::class.java)
    }

    override fun handlerAdded(ctx: ChannelHandlerContext?) {
        TODO("Not yet implemented")
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        TODO("Not yet implemented")
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun bind(ctx: ChannelHandlerContext?, localAddress: SocketAddress?, promise: ChannelPromise?) {
        TODO("Not yet implemented")
    }

    override fun connect(
        ctx: ChannelHandlerContext?,
        remoteAddress: SocketAddress?,
        localAddress: SocketAddress?,
        promise: ChannelPromise?
    ) {
        TODO("Not yet implemented")
    }

    override fun disconnect(ctx: ChannelHandlerContext?, promise: ChannelPromise?) {
        TODO("Not yet implemented")
    }

    override fun close(ctx: ChannelHandlerContext?, promise: ChannelPromise?) {
        TODO("Not yet implemented")
    }

    override fun deregister(ctx: ChannelHandlerContext?, promise: ChannelPromise?) {
        TODO("Not yet implemented")
    }

    override fun read(ctx: ChannelHandlerContext?) {
        TODO("Not yet implemented")
    }

    override fun write(ctx: ChannelHandlerContext?, msg: Any?, promise: ChannelPromise?) {
        TODO("Not yet implemented")
    }

    override fun flush(ctx: ChannelHandlerContext?) {
        TODO("Not yet implemented")
    }
}