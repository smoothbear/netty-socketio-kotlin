package io.smoothbear.socketio.scheduler

open class SchedulerKey (
    private val type: Type?,
    private val sessionId: Any?
) {

    enum class Type { PING_TIMEOUT, ACK_TIMEOUT, UPGRADE_TIMEOUT }

    override fun hashCode(): Int {
        val prime = 31;
        var result = 1

        result = prime * result + (sessionId?.hashCode() ?: 0)
        result = prime * result + (type?.hashCode() ?: 0)

        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SchedulerKey

        if (type != other.type) return false
        if (sessionId != other.sessionId) return false

        return true
    }
}