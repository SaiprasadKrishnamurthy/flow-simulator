package org.sai.simulator.flow.service.scheduler

import org.sai.simulator.flow.model.TaskSchedulerService
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ScheduledFuture

/**
 * Service that dynamically schedules and un-schedules a task.
 *
 * @author Sai.
 */
@Service
class DefaultSchedulerService(private val taskScheduler: TaskScheduler) : TaskSchedulerService {
    private val jobsMap: ConcurrentHashMap<String, ScheduledFuture<*>?> =
        ConcurrentHashMap<String, ScheduledFuture<*>?>()

    override fun scheduleTask(jobId: String, tasklet: Runnable, cronExpression: String) {
        val task = taskScheduler.schedule(
            tasklet,
            CronTrigger(cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().id))
        )
        jobsMap[jobId] = task!!
    }

    override fun removeTask(jobId: String) {
        jobsMap.computeIfPresent(jobId) { _, task ->
            task.cancel(false)
            null
        }
    }
}