package org.anymind.ecommerce.pos.controller

import org.anymind.ecommerce.pos.dto.CalculationRequest
import org.anymind.ecommerce.pos.service.PosCalculationService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class CalculationHandler(private val posCalculator: PosCalculationService) {
    fun calculate(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(CalculationRequest::class.java)
            .flatMap { posCalculator.calculate(it) }
            .flatMap { ServerResponse.ok().bodyValue(it) }
            .onErrorResume { e ->
                ServerResponse.badRequest().bodyValue(mapOf("error" to e.message))
            }
    }
}