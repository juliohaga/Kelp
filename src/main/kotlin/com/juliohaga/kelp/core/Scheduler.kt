package com.juliohaga.kelp.core

import com.juliohaga.kelp.core.component.Component
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

@Component
class Scheduler(private val plugin: Plugin) {

    private val scheduler
        get() = plugin.server.scheduler

    fun sync(task: () -> Unit): BukkitTask =
        scheduler.runTask(plugin, Runnable(task))

    fun async(task: () -> Unit): BukkitTask =
        scheduler.runTaskAsynchronously(plugin, Runnable(task))

    fun delay(ticks: Long, task: () -> Unit): BukkitTask =
        scheduler.runTaskLater(plugin, Runnable(task), ticks)

    fun delayAsync(ticks: Long, task: () -> Unit): BukkitTask =
        scheduler.runTaskLaterAsynchronously(plugin, Runnable(task), ticks)

    fun repeat(
        period: Long,
        task: () -> Unit
    ): BukkitTask =
        repeat(0L, period, task)

    fun repeat(
        delay: Long,
        period: Long,
        task: () -> Unit
    ): BukkitTask =
        scheduler.runTaskTimer(
            plugin,
            Runnable(task),
            delay,
            period
        )

    fun repeatAsync(
        period: Long,
        task: () -> Unit
    ): BukkitTask =
        repeatAsync(0L, period, task)

    fun repeatAsync(
        delay: Long,
        period: Long,
        task: () -> Unit
    ): BukkitTask =
        scheduler.runTaskTimerAsynchronously(
            plugin,
            Runnable(task),
            delay,
            period
        )

    fun cancel(task: BukkitTask) {
        task.cancel()
    }

    fun cancel(taskId: Int) {
        scheduler.cancelTask(taskId)
    }

    fun cancelAll() {
        scheduler.cancelTasks(plugin)
    }
}