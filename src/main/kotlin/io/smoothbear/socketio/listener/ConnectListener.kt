package io.smoothbear.socketio.listener

import io.smoothbear.socketio.SocketIOClient

interface ConnectListener {
    fun onConnect(client: SocketIOClient)
}