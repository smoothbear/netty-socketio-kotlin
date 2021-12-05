package io.smoothbear.socketio.store

import io.smoothbear.socketio.Disconnectable
import io.smoothbear.socketio.store.pubsub.PubSubStore

interface StoreFactory : Disconnectable {
    fun pubSubStore(): PubSubStore
    fun <K, V> createMap(name: String): Map<K, V>

}