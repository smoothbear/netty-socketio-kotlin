package io.smoothbear.socketio.store.pubsub

import java.io.Serializable

abstract class PubSubMessage : Serializable {

    private var nodeId: Long? = null

    fun getNodeId(): Long? {
        return nodeId
    }

    fun setNodeId(nodeId: Long) {
        this.nodeId = nodeId
    }
}