package io.smoothbear.socketio.listener

import io.netty.channel.ChannelHandlerContext
import io.smoothbear.socketio.SocketIOClient

interface ExceptionListener {

    fun onEventException(e: Exception, args: List<Any>, client: SocketIOClient)

    fun onDisconnectException(e: Exception, client: SocketIOClient)

    fun onConnectException(e: Exception, client: SocketIOClient)

    fun onPingException(e: Exception, client: SocketIOClient)

    fun exceptionCaught(ctx: ChannelHandlerContext, e: Throwable): Boolean
}