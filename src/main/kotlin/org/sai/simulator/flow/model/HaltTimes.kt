package org.sai.simulator.flow.model

data class HaltTimes(val from: Zone, val station: StationGroup, val percentageTraffic: Double, val haltTimeInMinutes: Long)
