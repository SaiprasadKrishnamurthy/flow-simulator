package org.sai.simulator.flow.model

import org.springframework.context.ApplicationEvent
import java.time.LocalDateTime

data class JobFinishedEvent(val job: Job): ApplicationEvent(job)