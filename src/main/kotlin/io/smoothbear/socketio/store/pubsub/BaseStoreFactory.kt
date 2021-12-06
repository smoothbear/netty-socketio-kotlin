package io.smoothbear.socketio.store.pubsub

import io.smoothbear.socketio.store.StoreFactory
import org.slf4j.LoggerFactory

abstract class BaseStoreFactory : StoreFactory {

    companion object {
        @JvmField
        val logger = LoggerFactory.getLogger(BaseStoreFactory::class.java)
    }

    var nodeId: Long = (Math.random() * 1000000).toLong()

    over
}