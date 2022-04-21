package org.sai.simulator.flow.listeners

import org.sai.simulator.flow.model.CalculateTrafficCommand
import org.sai.simulator.flow.model.ClockTickEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ClockTickEventListener(private val applicationEventPublisher: ApplicationEventPublisher) {

    @EventListener
    fun clockTickEventReceived(clockTickEvent: ClockTickEvent) {
        val zone = clockTickEvent.job.startZone!!
        applicationEventPublisher.publishEvent(
            CalculateTrafficCommand(
                job = clockTickEvent.job,
                time = clockTickEvent.time,
                zone = zone
            )
        )
    }
}