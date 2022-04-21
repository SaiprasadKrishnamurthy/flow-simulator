package org.sai.simulator.flow.listeners

import org.sai.simulator.flow.model.CalculateTrafficCommand
import org.sai.simulator.flow.model.Traffic
import org.sai.simulator.flow.model.TrafficCalculatedEvent
import org.sai.simulator.flow.repository.TrafficRepository
import org.sai.simulator.flow.service.realtime.DefaultRealTimeDataService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import kotlin.math.roundToLong
import kotlin.random.Random

@Component
class CalculateTrafficCommandListener(
    private val defaultRealTimeDataService: DefaultRealTimeDataService,
    private val trafficRepository: TrafficRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    @EventListener
    fun calculateTrafficCommandReceived(calculateTrafficCommand: CalculateTrafficCommand) {

        val travelTimes = defaultRealTimeDataService.getTravelTimes(calculateTrafficCommand)
        val haltsPercentage = defaultRealTimeDataService.getHaltsPercentage(calculateTrafficCommand)

        var trafficCount = calculateTrafficCommand.trafficCount

        // insert the traffic at this point for genesis zones.
        if (calculateTrafficCommand.genesisZone) {
            trafficCount = defaultRealTimeDataService.getTrafficCount(calculateTrafficCommand)
            trafficRepository.saveAndFlush(
                Traffic(
                    dateTime = calculateTrafficCommand.time,
                    job = calculateTrafficCommand.job,
                    zone = calculateTrafficCommand.zone,
                    trafficCount = trafficCount
                )
            )
        }
        // Traffic with halts.
        var trafficCountHalts = 0L
        haltsPercentage.forEach { hp ->
            trafficCountHalts =
                ((hp.percentageTraffic / 100.0) * trafficCount).roundToLong()
            val haltTimeInCurrentZone = hp.haltTimeInMinutes
            //println(" ${hp.percentageTraffic}% of $trafficCount === $trafficCountHalts halts at ===== $haltTimeInCurrentZone ==== ${hp.station.name}  capacity ${hp.station.capacity}")
            if (hp.station.capacity >= trafficCountHalts) {
                travelTimes.forEach { tt ->
                    val futuretime = tt.travelTimeInMinutes + haltTimeInCurrentZone
                    val nextZone = tt.to
                    val trafficGoesToNextZone =
                        ((tt.percentageTraffic / 100.0) * trafficCountHalts.toDouble()).roundToLong()
                    trafficRepository.saveAndFlush(
                        Traffic(
                            dateTime = calculateTrafficCommand.time.plusMinutes(futuretime),
                            job = calculateTrafficCommand.job,
                            zone = nextZone,
                            trafficCount = trafficGoesToNextZone
                        )
                    )

                    // deduct the traffic from the current zone as it would have moved to the next zone.
                    trafficRepository.saveAndFlush(
                        Traffic(
                            dateTime = calculateTrafficCommand.time.plusMinutes(futuretime),
                            job = calculateTrafficCommand.job,
                            zone = calculateTrafficCommand.zone,
                            trafficCount = -(trafficGoesToNextZone)
                        )
                    )
                    applicationEventPublisher.publishEvent(
                        calculateTrafficCommand.copy(
                            time = calculateTrafficCommand.time.plusMinutes(futuretime),
                            zone = nextZone,
                            trafficCount = trafficGoesToNextZone,
                            genesisZone = false,
                        )
                    )
                }
            } else {
                // The halt count is more than the station capacity. Then we process the batch of size capacity sequentially.
                travelTimes.forEach { tt ->
                    val nextZone = tt.to
                    val capacity = hp.station.capacity
                    val count = trafficCountHalts
                    val batches = (count / capacity)
                    val residue = (count % capacity)

                    // For each batch,
                    for (i in 1..batches) {
                        val futuretime =
                            calculateTrafficCommand.time.plusMinutes(tt.travelTimeInMinutes + (i * haltTimeInCurrentZone))
                                .plusMinutes(Random.nextLong(hp.station.randomBufferTimeInMinutes))
                        // Traffic that goes to the next zone.
                        trafficRepository.saveAndFlush(
                            Traffic(
                                dateTime = futuretime,
                                job = calculateTrafficCommand.job,
                                zone = nextZone,
                                trafficCount = capacity.toLong()
                            )
                        )
                        // Traffic that should be removed from the current zone.
                        trafficRepository.saveAndFlush(
                            Traffic(
                                dateTime = futuretime,
                                job = calculateTrafficCommand.job,
                                zone = calculateTrafficCommand.zone,
                                trafficCount = -(capacity.toLong())
                            )
                        )
                    }
                }

            }
        }

        // Traffic counts without halts
        val trafficWithoutHalts = trafficCount - trafficCountHalts
        travelTimes.forEach { tt ->
            val nextZone = tt.to
            val trafficGoesToNextZone =
                ((tt.percentageTraffic / 100.0) * trafficWithoutHalts.toDouble()).roundToLong()
            trafficRepository.saveAndFlush(
                Traffic(
                    dateTime = calculateTrafficCommand.time.plusMinutes(tt.travelTimeInMinutes),
                    job = calculateTrafficCommand.job,
                    zone = nextZone,
                    trafficCount = trafficGoesToNextZone
                )
            )

            // deduct the traffic from the current zone as it would have moved to the next zone.
            trafficRepository.saveAndFlush(
                Traffic(
                    dateTime = calculateTrafficCommand.time.plusMinutes(tt.travelTimeInMinutes),
                    job = calculateTrafficCommand.job,
                    zone = calculateTrafficCommand.zone,
                    trafficCount = -(trafficGoesToNextZone)
                )
            )
        }
        applicationEventPublisher.publishEvent(
            TrafficCalculatedEvent(
                job = calculateTrafficCommand.job,
                calculateTrafficCommand.time
            )
        )
    }
}