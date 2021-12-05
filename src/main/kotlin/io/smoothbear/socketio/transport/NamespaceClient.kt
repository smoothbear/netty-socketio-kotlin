package io.smoothbear.socketio.transport

import io.smoothbear.socketio.SocketIOClient
import io.smoothbear.socketio.handler.ClientHead
import io.smoothbear.socketio.namespace.Namespace
import java.util.concurrent.atomic.AtomicBoolean

class NamespaceClient (
    val baseClient: ClientHead,
    val namespace: Namespace
) : SocketIOClient {

    init {
        namespace.addClient(this)
    }

    private val disconnected: AtomicBoolean = AtomicBoolean()
}