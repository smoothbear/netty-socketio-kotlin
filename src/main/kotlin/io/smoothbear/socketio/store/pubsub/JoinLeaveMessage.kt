package io.smoothbear.socketio.store.pubsub

import java.util.*

class JoinLeaveMessage (
    val sessionId: UUID,
    val namespace: String,
    val room: String
) : PubSubMessage()