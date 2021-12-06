package io.smoothbear.socketio.listener

import io.smoothbear.socketio.SocketIOClient

interface DisconnectListener {
    fun onDisconnect(client: SocketIOClient)
}