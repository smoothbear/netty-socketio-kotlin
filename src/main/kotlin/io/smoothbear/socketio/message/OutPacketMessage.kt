package io.smoothbear.socketio.message

import io.smoothbear.socketio.Transport
import io.smoothbear.socketio.handler.ClientHead

class OutPacketMessage (
    val clientHead: ClientHead,
    val transport: Transport
) : HttpMessage(clientHead.getOrigin(), clientHead.sessionId)