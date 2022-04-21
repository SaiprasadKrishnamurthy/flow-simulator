package org.sai.simulator.flow.listeners

import org.sai.simulator.flow.model.JobFinishedEvent
import org.sai.simulator.flow.service.scheduler.DefaultSchedulerService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class JobFinishedEventListener(private val schedulerService: DefaultSchedulerService) {

    @EventListener
    fun jobFinishedEventReceived(jobFinishedEvent: JobFinishedEvent) {
        schedulerService.removeTask(jobFinishedEvent.job.id.toString())
    }
}