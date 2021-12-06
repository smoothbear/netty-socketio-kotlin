package io.smoothbear.socketio

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

open class BroadcastAckCallback<T> (
    private val resultClass: Class<T>,
    private val timeout: Int
) {

    public constructor(resultClass: Class<T>) : this(resultClass, -1)

    private val loopFinished: AtomicBoolean = AtomicBoolean()
    private val counter: AtomicInteger = AtomicInteger()
    private val successExecuted: AtomicBoolean = AtomicBoolean()

    fun createClientCallback(client: SocketIOClient): AckCallback<T> {
        counter.getAndIncrement()

        return object : AckCallback<T>(resultClass, timeout) {

            override fun onSuccess(result: T) {
                counter.getAndDecrement()
                onClientSuccess(client, result)
                executeSuccess()
            }

            override fun onTimeout() {
                onClientTimeout(client)
            }
        }
    }

    protected fun onClientTimeout(client: SocketIOClient) {}

    protected fun onClientSuccess(client: SocketIOClient, result: T) {}

    protected fun onAllSuccess() {}

    private fun executeSuccess() {
        if (loopFinished.get()
            && counter.get() == 0 && successExecuted.compareAndSet(false, true)
        ) {
            onAllSuccess()
        }
    }

    fun loopFinished() {
        loopFinished.set(true)
        executeSuccess()
    }
}