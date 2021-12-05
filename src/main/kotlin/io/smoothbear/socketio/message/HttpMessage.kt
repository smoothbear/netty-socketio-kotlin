package io.smoothbear.socketio.message

import java.util.*

abstract class HttpMessage (
    val origin: String,
    val sessionId: UUID
)