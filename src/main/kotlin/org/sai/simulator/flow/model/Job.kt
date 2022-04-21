package org.sai.simulator.flow.model

import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "JOBS")
data class Job(

    @Id
    val id: Long = 0,

    val reportTaskId: String = UUID.randomUUID().toString(),

    @OneToOne
    val startZone: Zone? = null,

    val fromTime: LocalDateTime? = null,

    val toTime: LocalDateTime? = null,

    val clockTickSpeed: Long = 2,

    val processingDuration: Long = 4,

    val reportFrequency: Long = 1,

    val active: Boolean = true
)
