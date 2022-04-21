package org.sai.simulator.flow.repository

import org.sai.simulator.flow.model.Job
import org.sai.simulator.flow.model.Traffic
import org.sai.simulator.flow.model.TrafficAggregationResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TrafficRepository : JpaRepository<Traffic, String> {
    // TODO revisit this.
    /*@Query("SELECT new org.sai.simulator.flow.model.TrafficAggregationResult(t.zone.id,  sum(trafficCount)) from Traffic t group by t.zone")
    fun trafficAggregation(): List<TrafficAggregationResult>*/

    @Query("select t from Traffic t where t.dateTime <= :#{#time} and t.job = :#{#job}")
    fun findTraffic(@Param("job") job: Job, @Param("time") time: LocalDateTime): List<Traffic>
}