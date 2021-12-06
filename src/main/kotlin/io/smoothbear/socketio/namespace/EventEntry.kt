package io.smoothbear.socketio.namespace

import io.smoothbear.socketio.listener.DataListener
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class EventEntry<T> {

    private val listeners: Queue<DataListener<T>> = ConcurrentLinkedQueue()

    fun addListener(listener: DataListener<T>) {
        listeners += listener
    }

    fun getListeners(): Queue<DataListener<T>> {
        return listeners
    }
}