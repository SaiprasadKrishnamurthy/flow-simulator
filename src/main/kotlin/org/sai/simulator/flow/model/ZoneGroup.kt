package org.sai.simulator.flow.model

import javax.persistence.*
import javax.persistence.Entity

@Entity
@Table(name = "ZONE_GROUP")
data class ZoneGroup(

    @Id
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val locationPolygon: String = "",
    val maxCapacity: Int = 10000,
    val end: Boolean = false,
)
