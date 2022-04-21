package org.sai.simulator.flow.repository

import org.sai.simulator.flow.model.Job
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JobRepository : JpaRepository<Job, Long>