package org.sai.simulator.flow.model

import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "TRAFFIC")
data class Traffic(

    @Id
    val id: String = UUID.randomUUID().toString(),

    val dateTime: LocalDateTime? = null,

    @ManyToOne
    val job: Job? = null,

    @ManyToOne
    val zone: Zone? = null,

    val trafficCount: Long = 0,
)
