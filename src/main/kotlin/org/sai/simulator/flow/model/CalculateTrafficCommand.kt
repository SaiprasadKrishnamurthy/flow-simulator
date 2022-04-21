package org.sai.simulator.flow.model

import org.springframework.context.ApplicationEvent
import java.time.LocalDateTime

data class CalculateTrafficCommand(
    val job: Job,
    val time: LocalDateTime,
    val zone: Zone,
    val genesisZone: Boolean = true,
    val trafficCount: Long = 0L
) : ApplicationEvent(job)