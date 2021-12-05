package io.smoothbear.socketio.store.pubsub

enum class PubSubType {

    CONNECT, DISCONNECT, JOIN, LEAVE, DISPATCH;

    override fun toString(): String {
        return name.lowercase()
    }
}