package io.smoothbear.socketio.store

interface Store {
    fun set(key: String, obj: Any)
    fun <T> get(key: String): T
    fun has(key: String): Boolean
    fun del(key: String)
}