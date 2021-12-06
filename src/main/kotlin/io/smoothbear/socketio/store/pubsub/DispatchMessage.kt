package io.smoothbear.socketio.store.pubsub

import io.smoothbear.socketio.protocol.Packet

class DispatchMessage (
    val room: String,
    val packet: Packet,
    val namespace: String
) : PubSubMessage()