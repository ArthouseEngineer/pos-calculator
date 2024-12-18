package org.anymind.ecommerce.pos.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.anymind.ecommerce.pos.dto.CalculationRequest
import org.anymind.ecommerce.pos.dto.CalculationResponse
import org.anymind.ecommerce.pos.entity.PaymentEntity
import org.anymind.ecommerce.pos.validator.DynamicValidationService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class CalculationService(
    private val validationService: DynamicValidationService,
    private val paymentMethodService: PaymentMethodService,
    private val paymentService: PaymentService,
) {
    fun calculate(request: CalculationRequest): Mono<CalculationResponse> {
        return validationService.validate(request)
            .then(paymentMethodService.findByName(request.paymentMethod))
            .flatMap { paymentMethod ->
                val finalPrice = request.price * paymentMethod.priceModifier
                val points = (request.price * paymentMethod.pointsModifier).toInt()

                val payment = PaymentEntity(
                    customerId = request.customerId,
                    paymentMethodId = paymentMethod.id!!,
                    price = request.price,
                    finalPrice = finalPrice,
                    points = points.toBigDecimal(),
                    additionalDetails = ObjectMapper().valueToTree(request.additionalItem),
                    datetime = request.datetime
                )

                paymentService.savePayment(payment).map {
                    CalculationResponse(finalPrice, points)
                }
            }
    }

}

