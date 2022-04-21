package org.sai.simulator.flow.service.scheduler

import org.sai.simulator.flow.model.JobFinishedEvent
import org.sai.simulator.flow.model.TrafficReportEvent
import org.sai.simulator.flow.repository.JobRepository
import org.springframework.context.ApplicationContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class TrafficReporterTask(
    private val jobId: Long,
    private val applicationContext: ApplicationContext,
    private val reportFrequency: Long,
) : Runnable {

    private var curr: LocalDateTime? = null

    override fun run() {
        val jobRepository = applicationContext.getBean(JobRepository::class.java)
        val job = jobRepository.findById(jobId)
        if (job.isPresent && job.get().active) {
            if (curr == null) {
                curr = job.get().fromTime!!
            } else {
                curr!!.plusMinutes(reportFrequency)
            }
            curr = curr!!.plusMinutes(1).truncatedTo(ChronoUnit.MINUTES)
            applicationContext.publishEvent(TrafficReportEvent(job = job.get(), time = curr!!))
        } else {
            applicationContext.publishEvent(JobFinishedEvent(job = job.get()))
        }
    }
}