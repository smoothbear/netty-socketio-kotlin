package io.smoothbear.socketio.namespace

import io.netty.util.internal.PlatformDependent
import io.smoothbear.socketio.Configuration
import io.smoothbear.socketio.SocketIONamespace
import java.util.concurrent.ConcurrentMap

class NamespacesHub (
    private val configuration: Configuration
) {

    private val namespaces: ConcurrentMap<String, SocketIONamespace> = PlatformDependent.newConcurrentHashMap()

    fun create(name: String): Namespace {
        val namespace = namespaces[name] as Namespace

        if (namespace == null) {
            namespace = Namespace(name, configuration)
        }
    }
}