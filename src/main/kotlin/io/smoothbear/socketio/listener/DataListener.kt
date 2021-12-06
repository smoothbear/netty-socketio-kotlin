package io.smoothbear.socketio.listener

import io.smoothbear.socketio.AckRequest
import io.smoothbear.socketio.SocketIOClient

interface DataListener<T> {
    fun onData(client: SocketIOClient, data: T, ackSender: AckRequest)
}