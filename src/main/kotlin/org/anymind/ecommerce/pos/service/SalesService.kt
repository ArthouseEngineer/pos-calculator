package org.anymind.ecommerce.pos.service

import org.anymind.ecommerce.pos.dto.SalesRequest
import org.anymind.ecommerce.pos.dto.SalesResponse
import org.anymind.ecommerce.pos.repository.PaymentRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux


@Service
class SalesService(private val paymentRepository: PaymentRepository) {

    fun getSalesByDateRange(request: SalesRequest): Flux<SalesResponse> {
        return paymentRepository.findSalesByHourAndPaymentMethod(request.startDateTime, request.endDateTime)
            .map { aggregate ->
                SalesResponse(
                    paymentMethod = aggregate.paymentMethod,
                    datetime = aggregate.datetime,
                    sales = aggregate.totalSales,
                    points = aggregate.totalPoints
                )
            }
    }
}