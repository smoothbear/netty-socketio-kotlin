package io.smoothbear.socketio.namespace

import io.netty.util.internal.PlatformDependent
import io.smoothbear.socketio.SocketIOClient
import io.smoothbear.socketio.SocketIONamespace
import java.util.*

class Namespace : SocketIONamespace {

    companion object {
        const val DEFAULT_NAME = ""
    }

    private val allClients: MutableMap<UUID, SocketIOClient> = PlatformDependent.newConcurrentHashMap()

    fun addClient(client: SocketIOClient) {
        allClients[client.getSessionId()] = client
    }
}