package org.sai.simulator.flow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@EnableJpaRepositories
@EnableAsync
@EnableScheduling
@SpringBootApplication
class FlowSimulatorApplication

fun main(args: Array<String>) {
	runApplication<FlowSimulatorApplication>(*args)
}
