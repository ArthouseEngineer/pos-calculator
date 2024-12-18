package org.anymind.ecommerce.pos.service

import org.anymind.ecommerce.pos.dto.CalculationRequest
import org.anymind.ecommerce.pos.dto.CalculationResponse
import reactor.core.publisher.Mono

interface CalculationService {
    fun calculate(request: CalculationRequest): Mono<CalculationResponse>
}
