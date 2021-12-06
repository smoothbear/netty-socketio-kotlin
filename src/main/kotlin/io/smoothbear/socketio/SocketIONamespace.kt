package io.smoothbear.socketio

import io.smoothbear.socketio.listener.ClientListeners
import java.util.*


interface SocketIONamespace : ClientListeners {

    fun getName(): String

    fun getBroadcastOperations(): BroadcastOperations

    fun getRoomOperations(room: String): BroadcastOperations

    /**
     * Get all clients connected to namespace
     *
     * @return collection of clients
     */
    fun getAllClients(): Collection<SocketIOClient>

    /**
     * Get client by uuid connected to namespace
     *
     * @param uuid - id of client
     * @return client
     */
    fun getClient(uuid: UUID): SocketIOClient?
}