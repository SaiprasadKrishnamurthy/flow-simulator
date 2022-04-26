package org.sai.simulator.flow.repository

import org.sai.simulator.flow.model.ZoneGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ZoneGroupRepository : JpaRepository<ZoneGroup, Long>