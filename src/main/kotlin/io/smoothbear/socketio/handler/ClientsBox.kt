package io.smoothbear.socketio.handler

import io.netty.channel.Channel
import io.netty.util.internal.PlatformDependent
import io.smoothbear.socketio.HandShakeData
import java.util.*

class ClientsBox {

    private val uuid2clients: MutableMap<UUID, ClientHead> = PlatformDependent.newConcurrentHashMap()
    private val channel2clients: MutableMap<Channel, ClientHead> = PlatformDependent.newConcurrentHashMap()

    fun getHandshakeData(sessionId: UUID): HandShakeData? {
        val client = uuid2clients[sessionId]

        return client?.handshakeData
    }

    fun addClient(clientHead: ClientHead) {
        uuid2clients[clientHead.sessionId] = clientHead
    }

    fun removeClient(sessionId: UUID) {
        uuid2clients.remove(sessionId)
    }

    fun get(sessionId: UUID): ClientHead? {
        return uuid2clients[sessionId]
    }

    fun add(channel: Channel, clientHead: ClientHead) {
        channel2clients[channel] = clientHead
    }

    fun remove(channel: Channel) {
        channel2clients.remove(channel)
    }

    fun get(channel: Channel): ClientHead? = channel2clients[channel]
}