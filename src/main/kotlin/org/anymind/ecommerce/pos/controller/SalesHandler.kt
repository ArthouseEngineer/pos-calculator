package org.anymind.ecommerce.pos.controller

import org.anymind.ecommerce.pos.dto.SalesRequest
import org.anymind.ecommerce.pos.dto.SalesResponse
import org.anymind.ecommerce.pos.service.SalesService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class SalesHandler(private val salesService: SalesService) {
    fun getSalesByDateRange(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(SalesRequest::class.java)
            .flatMapMany { dto ->
                salesService.getSalesByDateRange(dto)
                    .map { aggregate ->
                        SalesResponse(
                            paymentMethod = aggregate.paymentMethod,
                            datetime = aggregate.datetime,
                            sales = aggregate.sales,
                            points = aggregate.points
                        )
                    }
            }
            .collectList()
            .flatMap { salesList ->
                ServerResponse.ok().bodyValue(mapOf("sales" to salesList))
            }
    }
}