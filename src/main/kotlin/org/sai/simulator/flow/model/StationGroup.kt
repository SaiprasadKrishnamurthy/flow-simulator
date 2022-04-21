package org.sai.simulator.flow.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "STATION_GROUP")
data class StationGroup(
    @Id
    val id: Long = System.nanoTime(),
    @ManyToOne(optional = true)
    val zone: Zone? = null,
    val name: String = "",
    val description: String = "'",
    val category: String = "",
    val capacity: Int = 0,
    val randomBufferTimeInMinutes: Long = 4L,
)
