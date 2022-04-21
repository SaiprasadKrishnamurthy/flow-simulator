package org.sai.simulator.flow.model

import org.springframework.context.ApplicationEvent
import java.time.LocalDateTime

data class TrafficReportEvent(val job: Job, val time: LocalDateTime) : ApplicationEvent(job)