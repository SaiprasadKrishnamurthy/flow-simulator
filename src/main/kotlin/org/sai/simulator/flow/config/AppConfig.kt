package org.sai.simulator.flow.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ApplicationEventMulticaster
import org.springframework.context.event.SimpleApplicationEventMulticaster
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.util.*

@EnableAsync
@EnableScheduling
@Configuration
class AppConfig {

    @Bean(name = ["applicationEventMulticaster"])
    fun simpleApplicationEventMulticaster(): ApplicationEventMulticaster {
        val eventMulticaster = SimpleApplicationEventMulticaster()
        eventMulticaster.setTaskExecutor(SimpleAsyncTaskExecutor())
        return eventMulticaster
    }

    @Bean
    fun corsFilter(): CorsFilter? {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.allowedOriginPatterns = listOf("*")
        config.allowedHeaders = Arrays.asList("*")
        config.allowedMethods = Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}