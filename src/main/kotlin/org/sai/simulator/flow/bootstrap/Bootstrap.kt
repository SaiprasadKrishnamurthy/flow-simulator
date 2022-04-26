package org.sai.simulator.flow.bootstrap

import org.sai.simulator.flow.model.*
import org.sai.simulator.flow.repository.*
import org.sai.simulator.flow.service.scheduler.ClockTickerTask
import org.sai.simulator.flow.service.scheduler.TrafficReporterTask
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class Bootstrap {

    @Bean
    fun onStart(
        taskSchedulerService: TaskSchedulerService,
        applicationContext: ApplicationContext,
        jobRepository: JobRepository,
        zoneRepository: ZoneRepository,
        zoneGroupRepository: ZoneGroupRepository,
        pathRepository: PathRepository,
        stationGroupRepository: StationGroupRepository
    ) = CommandLineRunner {
        val zg1 = ZoneGroup(id = 1, "Checkin", "Checkin", "", 1000)
        val zg2 = ZoneGroup(id = 2, "Security", "Security", "", 1000)
        val zg3 = ZoneGroup(id = 3, "Gates", "Gates", "", 1000, end = true)
        val a = Zone(id = 1, "Zone A", "Zone A", "", 1000, zoneGroup = zg1)
        val b = Zone(id = 2, "Zone B", "Zone B", "", 1000, zoneGroup = zg2)
        val c = Zone(id = 3, "Zone C", "Zone C", "", 1000, zoneGroup = zg3)
        zoneGroupRepository.saveAllAndFlush(listOf(zg1, zg2, zg3))
        zoneRepository.saveAllAndFlush(listOf(a, b, c))
        val path1 = Path(from = a, to = b)
        val path2 = Path(from = b, to = c)
        pathRepository.saveAndFlush(path1)
        pathRepository.saveAndFlush(path2)

        val statGroup = StationGroup(
            zone = a,
            name = "Washrooms on Zone A",
            description = "Washrooms present in the area zone a",
            category = "Washrooms",
            capacity = 10
        )

        stationGroupRepository.saveAndFlush(statGroup)

        val job = Job(
            id = 1,
            startZone = a,
            fromTime = LocalDateTime.now(),
            toTime = LocalDateTime.now().plusMinutes(10),
            clockTickSpeed = 1, // event listening speed in seconds. Every 'n' seconds the clock tick event is emitted.
            processingDuration = 3, // the interval in minutes. eg: 5, 10, 15, 20 ...etc minute (if the interval is 5).
            reportFrequency = 2L
        )

        jobRepository.saveAndFlush(job)

        // Collection task.
        taskSchedulerService.scheduleTask(
            job.id.toString(),
            ClockTickerTask(
                jobId = job.id,
                applicationContext = applicationContext,
                systemClockTickIntervalMinutes = job.processingDuration,
            ),
            cronExpression = "*/${job.clockTickSpeed} * * * * *"
        )

        // Report task.
        taskSchedulerService.scheduleTask(
            job.reportTaskId,
            TrafficReporterTask(
                jobId = job.id,
                applicationContext = applicationContext,
                reportFrequency = job.reportFrequency
            ),
            cronExpression = "*/${job.reportFrequency} * * * * *"
        )
    }
}