package io.smoothbear.socketio.ack

import io.smoothbear.socketio.scheduler.SchedulerKey
import java.util.*

class AckSchedulerKey(
    type: Type, sessionId: UUID, val index: Long
) : SchedulerKey(type, sessionId) {

    override fun hashCode(): Int {
        val prime = 31
        var result = super.hashCode()
        result = prime * result + (index xor (index ushr 32)).toInt()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as AckSchedulerKey

        if (index != other.index) return false

        return true
    }
}