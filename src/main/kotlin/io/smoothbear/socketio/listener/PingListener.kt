package io.smoothbear.socketio.listener

import io.smoothbear.socketio.SocketIOClient

interface PingListener {
    fun onPing(client: SocketIOClient)
}