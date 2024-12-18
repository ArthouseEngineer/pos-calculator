package org.anymind.ecommerce.pos.validator

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.anymind.ecommerce.pos.dto.CalculationRequest
import org.anymind.ecommerce.pos.service.PaymentMethodService
import org.anymind.ecommerce.pos.validator.exception.ValidationException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DynamicValidationService(
    private val paymentMethodService: PaymentMethodService,
    private val objectMapper: ObjectMapper
) {

    fun validate(request: CalculationRequest): Mono<Void> {
        return paymentMethodService.getValidationRules(request.paymentMethod)
            .doOnNext { println("Validation rules for payment method ${request.paymentMethod}: $it") }
            .flatMap { json ->
                val rules: JsonNode = objectMapper.readTree(json.asString())

                if (request.additionalItem == null) {
                    return@flatMap Mono.empty<Void>()
                }

                val requestJson: ObjectNode = objectMapper.valueToTree(request.additionalItem)
                validateFields(rules, requestJson, request.paymentMethod)
                Mono.empty()
            }
            .doOnSuccess { println("Validation passed for payment method ${request.paymentMethod}") }
    }

    private fun validateFields(
        rules: JsonNode,
        requestJson: ObjectNode,
        paymentMethod: String
    ) {
        rules["requiredFields"]?.map { it.asText() }?.forEach { field ->
            val valueNode = requestJson[field]
            if (valueNode?.isNull != false) {
                throw ValidationException(paymentMethod, "Field '$field' is required and cannot be null.")
            }
        }

        rules["customChecks"]?.fields()?.forEach { (field, checks) ->
            val valueNode = requestJson[field]
            if (valueNode?.isNull != false) {
                throw ValidationException(paymentMethod, "Field '$field' cannot be null.")
            }

            val value = valueNode.asText()

            checks["allowedValues"]?.map { it.asText() }?.let { allowedValues ->
                if (value !in allowedValues) {
                    throw ValidationException(
                        paymentMethod,
                        "Invalid value '$value' for field '$field'. Allowed: $allowedValues"
                    )
                }
            }
        }
    }

}




