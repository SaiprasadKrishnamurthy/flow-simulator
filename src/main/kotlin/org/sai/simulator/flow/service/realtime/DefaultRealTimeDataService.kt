package org.sai.simulator.flow.service.realtime

import org.sai.simulator.flow.model.CalculateTrafficCommand
import org.sai.simulator.flow.model.HaltTimes
import org.sai.simulator.flow.model.StationGroup
import org.sai.simulator.flow.model.TravelTimes
import org.sai.simulator.flow.repository.PathRepository
import org.sai.simulator.flow.repository.StationGroupRepository
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class DefaultRealTimeDataService(
    private val pathRepository: PathRepository,
    private val stationGroupRepository: StationGroupRepository
) {

    fun getTrafficCount(calculateTrafficCommand: CalculateTrafficCommand): Long {
        // if it's a genesis zone, then return a value else 0.
        return if (calculateTrafficCommand.genesisZone) Random.nextLong(50) else 0L
    }

    fun getTravelTimes(calculateTrafficCommand: CalculateTrafficCommand): List<TravelTimes> {
        val nextZones = pathRepository.findNextZonePaths(calculateTrafficCommand.zone).mapNotNull { it.to }

        // Generate at random.
        return nextZones.map {
            TravelTimes(
                from = calculateTrafficCommand.zone,
                to = it,
                percentageTraffic = 100.0,
                travelTimeInMinutes = 10
            )
        }
    }

    fun getHaltsPercentage(calculateTrafficCommand: CalculateTrafficCommand): List<HaltTimes> {
        return stationGroupRepository.findByZone(calculateTrafficCommand.zone).map { sg ->
            HaltTimes(
                from = calculateTrafficCommand.zone,
                station = sg,
                percentageTraffic = 20.0,
                haltTimeInMinutes = getHaltTimeInMinutes(calculateTrafficCommand, sg)
            )
        }
    }

    fun getHaltTimeInMinutes(calculateTrafficCommand: CalculateTrafficCommand, stationGroup: StationGroup): Long {
        return 10L
    }
}