package org.sai.simulator.flow.model

data class TrafficAggregationResult(val zone: Long? = null, val trafficCount: Long = 0)

data class TrafficReport(val job: Job? = null, val zone: Zone? = null, val trafficCount: Long = 0, val dateTime: Long? = null)