package org.sai.simulator.flow.listeners

import com.mitchtalmadge.asciidata.graph.ASCIIGraph
import org.sai.simulator.flow.model.TrafficReport
import org.sai.simulator.flow.model.TrafficReportEvent
import org.sai.simulator.flow.repository.TrafficRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneId

@Component
class TrafficReportEventListener(private val trafficRepository: TrafficRepository) {

    data class ZonewisePlot(val zone: String, val count: Long)

    private val plots = mutableMapOf<String, MutableList<ZonewisePlot>>()

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
        reps.filterNot { it.zone?.zoneGroup?.end ?: false }.forEach {
            println(
                "\t\t" + it.zone?.name + " - " + it.trafficCount + " - " + Instant.ofEpochMilli(it.dateTime!!).atZone(
                    ZoneId.systemDefault()
                ).toLocalDateTime()
            )
        }

        /*reps.forEach { ta ->
            plots.compute(trafficReportEvent.job.id.toString()) { _, value ->
                if (value == null) {
                    mutableListOf(ZonewisePlot(ta.zone?.name ?: "Zone", ta.trafficCount))
                } else {
                    value.add(ZonewisePlot(ta.zone?.name ?: "Zone", ta.trafficCount))
                    value
                }
            }
        }*/

        /*val g = plots[trafficReportEvent.job.id.toString()]?.groupBy { it.zone }

        ProcessBuilder("clear").inheritIO().start().waitFor()
        val ascii = g?.map { (z, plots) ->
            if (plots.size > 1) {
                z + "\n" + ASCIIGraph.fromSeries(plots.map { it.count.toDouble() }.toDoubleArray()).withNumRows(10)
                    .plot()
            } else {
                z
            }
        }
        if (ascii != null) {
            print(ascii.joinToString("\n"))
        }*/
    }
}