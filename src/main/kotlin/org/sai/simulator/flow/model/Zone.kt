package org.sai.simulator.flow.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "ZONE")
data class Zone(

    @Id
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val locationPolygon: String = "",
    val maxCapacity: Int = 10000,
    @ManyToOne(optional = true)
    val zoneGroup: ZoneGroup? = null,
)
