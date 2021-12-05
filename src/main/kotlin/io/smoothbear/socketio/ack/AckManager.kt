package io.smoothbear.socketio.ack

import io.netty.util.internal.PlatformDependent
import io.smoothbear.socketio.AckCallback
import io.smoothbear.socketio.Disconnectable
import io.smoothbear.socketio.handler.ClientHead
import io.smoothbear.socketio.scheduler.CancelableScheduler
import io.smoothbear.socketio.scheduler.SchedulerKey.Type
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

class AckManager (
    private val scheduler: CancelableScheduler
) : Disconnectable {

    private val ackEntries: ConcurrentMap<UUID, AckEntry> = PlatformDependent.newConcurrentHashMap()

    companion object {
        @JvmField
        val logger: Logger = LoggerFactory.getLogger(AckManager::class.java)
    }

    class AckEntry {
        private val ackCallbacks: MutableMap<Long, AckCallback<*>> = PlatformDependent.newConcurrentHashMap()
        private val ackIndex = AtomicLong(-1)

        fun addAckCallback(callback: AckCallback<*>): Long {
            val index = ackIndex.incrementAndGet()
            ackCallbacks[index] = callback
            return index
        }

        fun getAckIndexes(): Set<Long> = ackCallbacks.keys
        fun getAckCallback(index: Long): AckCallback<*>? = ackCallbacks[index]
        fun removeCallback(index: Long): AckCallback<*>? = ackCallbacks.remove(index)
        fun initAckIndex(index: Long) = ackIndex.compareAndSet(-1, index)
    }

    override fun onDisconnect(client: ClientHead) {
        val e = ackEntries.remove(client.)
    }

    fun initAckIndex(sessionId: UUID, index: Long) {
        val ackEntry = getAckEntry(sessionId)
        ackEntry.initAckIndex(index)
    }

    private fun getAckEntry(sessionId: UUID): AckEntry {
        var ackEntry = ackEntries[sessionId]

        if (ackEntry == null) {
            ackEntry = AckEntry()
            val oldAckEntry = ackEntries.putIfAbsent(sessionId, ackEntry)

            if (oldAckEntry != null)
                ackEntry = oldAckEntry
        }

        return ackEntry
    }

    fun removeClient(sessionId: UUID, index: Long): AckCallback<*>? {
        val ackEntry = ackEntries[sessionId]

        if (ackEntry != null)
            return ackEntry.removeCallback(index)

        return null
    }

    private fun removeCallback(sessionId: UUID, index: Long): AckCallback<*>? {
        val ackEntry = ackEntries[sessionId]

        if (ackEntry != null)
            return ackEntry.removeCallback(index)

        return null;
    }

    fun getCallback(sessionId: UUID, index: Long): AckCallback<*>? {
        val ackEntry = getAckEntry(sessionId)

        return ackEntry.getAckCallback(index)
    }

    fun registerAck(sessionId: UUID, callback: AckCallback<*>) {
        val ackEntry = getAckEntry(sessionId)
        ackEntry.initAckIndex(0)

        val index = ackEntry.addAckCallback(callback)

        if (logger.isDebugEnabled)
            logger.debug("AckCallback registered with id: $index for client: $sessionId")

        scheduleTimeout(index, sessionId, callback)
    }

    private fun scheduleTimeout(index: Long, sessionId: UUID, callback: AckCallback<*>) {
        if (callback.timeout == -1)
            return

        val key = AckSchedulerKey(Type.ACK_TIMEOUT, sessionId, index)

        scheduler.scheduleCallback(key, callback.timeout, TimeUnit.SECONDS) {
            removeCallback(sessionId, index)?.onTimeout()
        }
    }

}