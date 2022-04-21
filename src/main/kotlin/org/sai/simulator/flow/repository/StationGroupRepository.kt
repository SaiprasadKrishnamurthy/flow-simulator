package org.sai.simulator.flow.repository

import org.sai.simulator.flow.model.StationGroup
import org.sai.simulator.flow.model.Zone
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StationGroupRepository : JpaRepository<StationGroup, Long> {
    @Query("select p from StationGroup p where p.zone = :#{#zone}")
    fun findByZone(@Param("zone") zone: Zone): List<StationGroup>
}