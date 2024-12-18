package org.anymind.ecommerce.pos.controller

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class RouterConfig {
    @Bean
    fun calculator(handler: CalculationHandler): RouterFunction<ServerResponse> = router {
        POST("/api/calculate", handler::calculate)
    }

    @Bean
    fun sales(handler: SalesHandler): RouterFunction<ServerResponse> = router {
        POST("/api/sales", handler::getSalesByDateRange)
    }
}
