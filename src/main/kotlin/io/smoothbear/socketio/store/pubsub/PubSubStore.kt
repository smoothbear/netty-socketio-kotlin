package io.smoothbear.socketio.store.pubsub

interface PubSubStore {
    fun publish(type: PubSubType, msg: PubSubMessage)
}