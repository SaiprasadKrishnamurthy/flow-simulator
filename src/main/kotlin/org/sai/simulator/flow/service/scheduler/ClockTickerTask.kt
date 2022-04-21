package org.sai.simulator.flow.service.scheduler

import org.sai.simulator.flow.model.ClockTickEvent
import org.sai.simulator.flow.model.JobFinishedEvent
import org.sai.simulator.flow.repository.JobRepository
import org.springframework.context.ApplicationContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ClockTickerTask(
    private val jobId: Long,
    private val applicationContext: ApplicationContext,
    private val systemClockTickIntervalMinutes: Long
) : Runnable {

    private var curr: LocalDateTime? = null

    override fun run() {
        val jobRepository = applicationContext.getBean(JobRepository::class.java)
        val job = jobRepository.findById(jobId)
        if (job.isPresent && job.get().active) {
            if (curr != null && job.get().toTime!!.isBefore(curr)) {
                applicationContext.publishEvent(JobFinishedEvent(job = job.get()))
            } else {
                if (curr == null) {
                    curr = job.get().fromTime!!
                }
                curr = curr!!.plusMinutes(systemClockTickIntervalMinutes).truncatedTo(ChronoUnit.MINUTES)
                applicationContext.publishEvent(ClockTickEvent(job = job.get(), time = curr!!))
            }
        }
    }
}