package io.smoothbear.socketio

import io.smoothbear.socketio.handler.ClientHead

interface Disconnectable {
    fun onDisconnect(client: ClientHead)
}