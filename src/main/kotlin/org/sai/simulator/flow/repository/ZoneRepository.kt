package org.sai.simulator.flow.repository

import org.sai.simulator.flow.model.Zone
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ZoneRepository : JpaRepository<Zone, Long>