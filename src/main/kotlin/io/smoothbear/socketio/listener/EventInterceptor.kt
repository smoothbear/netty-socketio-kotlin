package io.smoothbear.socketio.listener

import io.smoothbear.socketio.AckRequest
import io.smoothbear.socketio.transport.NamespaceClient

interface EventInterceptor {
    fun onEvent(client: NamespaceClient, eventName: String, args: List<Any>, ackRequest: AckRequest)
}