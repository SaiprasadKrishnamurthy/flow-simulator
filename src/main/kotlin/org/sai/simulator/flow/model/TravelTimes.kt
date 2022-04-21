package org.sai.simulator.flow.model

data class TravelTimes(val from: Zone, val to: Zone, val percentageTraffic: Double, val travelTimeInMinutes: Long)