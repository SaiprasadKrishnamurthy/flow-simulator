package org.sai.simulator.flow.listeners

import org.sai.simulator.flow.model.TrafficReport
import org.sai.simulator.flow.model.TrafficReportEvent
import org.sai.simulator.flow.repository.TrafficRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneId

@Component
class TrafficReportEventListener(private val trafficRepository: TrafficRepository) {

    @EventListener
    fun trafficReportEventReceived(trafficReportEvent: TrafficReportEvent) {
        val trafficAggregation = trafficRepository.findTraffic(trafficReportEvent.job, trafficReportEvent.time)
        val grouped = trafficAggregation.groupBy { it.zone }.mapValues { it.value.sumOf { x -> x.trafficCount } }
        val reps = grouped.map { entry ->
            TrafficReport(
                job = trafficReportEvent.job,
                zone = entry.key,
                trafficCount = entry.value,
                dateTime = trafficReportEvent.time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
        }
        reps.filterNot { it.zone?.end ?: false }.forEach {
            println(
                "\t\t" + it.zone?.name + " - " + it.trafficCount + " - " + Instant.ofEpochMilli(it.dateTime!!).atZone(
                    ZoneId.systemDefault()
                ).toLocalDateTime()
            )
        }
    }
}