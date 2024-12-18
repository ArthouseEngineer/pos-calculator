package org.anymind.ecommerce.pos.service

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import io.r2dbc.postgresql.codec.Json
import org.anymind.ecommerce.pos.dto.CalculationRequest
import org.anymind.ecommerce.pos.dto.CalculationResponse
import org.anymind.ecommerce.pos.entity.PaymentEntity
import org.anymind.ecommerce.pos.validator.DynamicValidationService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime


@Service
class PosCalculationService(
    private val validationService: DynamicValidationService,
    private val paymentMethodService: PaymentMethodService,
    private val paymentService: PaymentService,
    private val objectMapper: ObjectMapper
) : CalculationService {
    override fun calculate(request: CalculationRequest): Mono<CalculationResponse> {
        return validationService.validate(request)
            .then(paymentMethodService.findByName(request.paymentMethod))
            .filter { it.active }.switchIfEmpty(
                Mono.error(
                    RuntimeException(
                        "We cant process this [${request.paymentMethod}] right now, sorry for inconvenience."
                    )
                )
            ).flatMap { paymentMethod ->
                val finalPrice = request.price * paymentMethod.priceModifier
                val points = request.price * paymentMethod.pointsModifier

                val paymentEntity = PaymentEntity(
                    customerId = request.customerId,
                    paymentMethodId = paymentMethod.id!!,
                    price = request.price,
                    finalPrice = finalPrice,
                    points = points,
                    additionalDetails = Json.of(objectMapper.writeValueAsString(request.additionalItem)),
                    datetime = request.datetime,
                    createdAt = LocalDateTime.now()
                )

                paymentService.savePayment(paymentEntity).map {
                    CalculationResponse(finalPrice, points)
                }
            }
    }

}

