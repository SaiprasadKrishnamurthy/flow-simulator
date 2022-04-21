package org.sai.simulator.flow.model

import javax.persistence.*
import javax.persistence.Entity

@Entity
@Table(name = "PATH")
data class Path(

    @Id
    val id: Long = System.nanoTime(),

    @OneToOne
    val from: Zone? = null,

    @OneToOne(optional = true)
    val to: Zone? = null
)
