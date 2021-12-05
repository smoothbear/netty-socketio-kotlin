package io.smoothbear.socketio

import io.smoothbear.socketio.transport.PollingTransport
import io.smoothbear.socketio.transport.WebSocketTransport

enum class Transport (
    val value: String
) {
    WEBSOCKET(WebSocketTransport.NAME),
    POLLING(PollingTransport.NAME);

    companion object {
        fun byName(value: String): Transport {
            for (t in values()) {
                if (t.value == value)
                    return t
            }

            throw IllegalArgumentException("Can't find $value transport")
        }
    }
}