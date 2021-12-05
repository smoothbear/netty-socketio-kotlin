package io.smoothbear.socketio.scheduler

import io.netty.channel.ChannelHandlerContext
import java.util.concurrent.TimeUnit

interface CancelableScheduler {
    fun update(ctx: ChannelHandlerContext)
    fun cancel(key: SchedulerKey)
    fun scheduleCallback(key: SchedulerKey, delay: Int, unit: TimeUnit, runnable: Runnable)
    fun schedule(delay: Int, unit: TimeUnit, runnable: Runnable)
    fun schedule(key: SchedulerKey, delay: Int, unit: TimeUnit, runnable: Runnable)
}