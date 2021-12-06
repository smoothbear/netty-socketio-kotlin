package io.smoothbear.socketio.listener

import io.netty.channel.ChannelHandlerContext
import io.smoothbear.socketio.SocketIOClient
import org.slf4j.LoggerFactory

class DefaultExceptionListener : ExceptionListenerAdapter() {

    companion object {
        @JvmField
        val logger = LoggerFactory.getLogger(DefaultExceptionListener::class.java)
    }

    override fun onEventException(e: Exception, args: List<Any>, client: SocketIOClient) {
        logger.error(e.message, e);
    }

    override fun onDisconnectException(e: Exception, client: SocketIOClient) {
        logger.error(e.message, e)
    }

    override fun onConnectException(e: Exception, client: SocketIOClient) {
        logger.error(e.message, e)
    }

    override fun onPingException(e: Exception, client: SocketIOClient) {
        logger.error(e.message, e)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, e: Throwable): Boolean {
        logger.error(e.message, e)
        return true
    }
}