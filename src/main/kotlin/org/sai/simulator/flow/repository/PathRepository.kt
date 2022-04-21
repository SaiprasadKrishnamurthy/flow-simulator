package org.sai.simulator.flow.repository

import org.sai.simulator.flow.model.Path
import org.sai.simulator.flow.model.Zone
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PathRepository : JpaRepository<Path, Long> {
    @Query("select p from Path p where p.from = :#{#zone}")
    fun findNextZonePaths(@Param("zone") zone: Zone): List<Path>
}