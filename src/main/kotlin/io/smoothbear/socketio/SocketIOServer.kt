package io.smoothbear.socketio

import io.netty.util.concurrent.Future
import io.smoothbear.socketio.listener.ClientListeners
import org.slf4j.LoggerFactory

class SocketIOServer (
    private val configuration: Configuration
) : ClientListeners {

    private val configCopy: Configuration

    companion object {
        @JvmField
        val logger = LoggerFactory.getLogger(SocketIOServer::class.java)
    }

    fun start() {

    }

    fun startAsync(): Future<Unit> {
        logger.info("Session store / pubsub factory used: ${configCopy.storeFactory()}");
    }
}