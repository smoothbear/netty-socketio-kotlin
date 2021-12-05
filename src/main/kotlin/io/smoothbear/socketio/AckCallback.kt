package io.smoothbear.socketio

import io.smoothbear.socketio.scheduler.SchedulerKey
import java.util.concurrent.TimeUnit

/**
 * Base ack callback class.
 *
 * Notifies about acknowledgement received from client
 * via {@link #onSuccess} callback method.
 *
 * By default it may wait acknowledgement from client
 * while {@link SocketIOClient} is alive. Timeout can be
 * defined {@link #timeout} as constructor argument.
 *
 * This object is NOT actual anymore if {@link #onSuccess} or
 * {@link #onTimeout} was executed.
 *
 * @param <T> - any serializable type
 *
 * @see VoidAckCallback
 * @see MultiTypeAckCallback
 *
 * Creates AckCallback with timeout
 * @param resultClass - result class
 * @param timeout - callback timeout in seconds
 */
abstract class AckCallback<T>(
    /**
     * Returns class of argument in {@link #onSuccess} method
     * @return - result class
     */
    val resultClass: Class<T>,
    val timeout: Int
) {

    /**
     * Create AckCallback
     * @param resultClass - result class
     */
    constructor(resultClass: Class<T>) : this(resultClass, -1)

    /**
     * Executes only once when acknowledgement received from client.
     * @param result - object sent by client
     */
    abstract fun onSuccess(result: T)

    /**
     * Invoked only once then <code>timeout</code> defined
     */
    open fun onTimeout() {}

}