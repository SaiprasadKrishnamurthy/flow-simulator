package org.sai.simulator.flow.model

import java.lang.Runnable

interface TaskSchedulerService {
    fun scheduleTask(jobId: String, tasklet: Runnable, cronExpression: String)
    fun removeTask(jobId: String)
}