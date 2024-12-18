package org.anymind.ecommerce.pos.validator

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.r2dbc.postgresql.codec.Json
import org.anymind.ecommerce.pos.dto.AdditionalItem
import org.anymind.ecommerce.pos.dto.BankDetails
import org.anymind.ecommerce.pos.dto.CalculationRequest
import org.anymind.ecommerce.pos.service.PaymentMethodService
import org.anymind.ecommerce.pos.validator.exception.ValidationException
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDateTime

class DynamicValidationServiceTest : StringSpec({

    val paymentMethodService = mockk<PaymentMethodService>()
    val objectMapper = ObjectMapper()
    val validationService = DynamicValidationService(paymentMethodService, objectMapper)

    "should pass validation for valid CASH_ON_DELIVERY request" {
        val validationRules = """
            {
                "requiredFields": ["courier"],
                "fieldTypes": { "courier": "string" },
                "customChecks": {
                    "courier": { "allowedValues": ["YAMATO", "SAGAWA"] }
                }
            }
        """
        every { paymentMethodService.getValidationRules("CASH_ON_DELIVERY") } returns Mono.just(Json.of(validationRules))

        val request = CalculationRequest(
            customerId = "12345",
            price = BigDecimal("100.00"),
            paymentMethod = "CASH_ON_DELIVERY",
            datetime = LocalDateTime.parse("2024-12-18T13:00:00"),
            additionalItem = AdditionalItem(courier = "YAMATO")
        )

        validationService.validate(request).block() shouldBe null
    }

    "should failed validation for unsupported delivery partner" {
        val validationRules = """
            {
                "requiredFields": ["courier"],
                "fieldTypes": { "courier": "string" },
                "customChecks": {
                    "courier": { "allowedValues": ["YAMATO", "SAGAWA"] }
                }
            }
        """
        every { paymentMethodService.getValidationRules("CASH_ON_DELIVERY") } returns Mono.just(Json.of(validationRules))

        val request = CalculationRequest(
            customerId = "12345",
            price = BigDecimal("100.00"),
            paymentMethod = "CASH_ON_DELIVERY",
            datetime = LocalDateTime.parse("2024-12-18T13:00:00"),
            additionalItem = AdditionalItem(courier = "NARUTO")
        )

        shouldThrow<ValidationException> {
            validationService.validate(request).block()
        }.message shouldBe "Validation failed for payment method [CASH_ON_DELIVERY]: Invalid value 'NARUTO' for field 'courier'. Allowed: [YAMATO, SAGAWA]"
    }

    "should fail validation when required field courier is missing" {
        val validationRules = """
            {
                "requiredFields": ["courier"],
                "fieldTypes": { "courier": "string" }
            }
        """
        every { paymentMethodService.getValidationRules("CASH_ON_DELIVERY") } returns Mono.just(Json.of(validationRules))

        val request = CalculationRequest(
            customerId = "12345",
            price = BigDecimal("100.00"),
            paymentMethod = "CASH_ON_DELIVERY",
            datetime = LocalDateTime.parse("2024-12-18T13:00:00"),
            additionalItem = AdditionalItem()
        )

        shouldThrow<ValidationException> {
            validationService.validate(request).block()
        }.message shouldBe "Validation failed for payment method [CASH_ON_DELIVERY]: Field 'courier' is required and cannot be null."
    }

    "should pass validation for valid BANK_TRANSFER request" {
        val validationRules = """
            {
                "requiredFields": ["bankDetails"],
                "fieldTypes": { "bankDetails": "object" }
            }
        """
        every { paymentMethodService.getValidationRules("BANK_TRANSFER") } returns Mono.just(Json.of(validationRules))

        val request = CalculationRequest(
            customerId = "12345",
            price = BigDecimal("200.00"),
            paymentMethod = "BANK_TRANSFER",
            datetime = LocalDateTime.parse("2024-12-18T13:00:00"),
            additionalItem = AdditionalItem(
                bankDetails = BankDetails(
                    bankName = "TestBank",
                    bankAccount = "123456789"
                )
            )
        )

        validationService.validate(request).block() shouldBe null
    }

    "should fail validation for missing bankDetails in BANK_TRANSFER request" {
        val validationRules = """
            {
                "requiredFields": ["bankDetails"],
                "fieldTypes": { "bankDetails": "object" }
            }
        """
        every { paymentMethodService.getValidationRules("BANK_TRANSFER") } returns Mono.just(Json.of(validationRules))

        val request = CalculationRequest(
            customerId = "12345",
            price = BigDecimal("200.00"),
            paymentMethod = "BANK_TRANSFER",
            datetime = LocalDateTime.parse("2024-12-18T13:00:00"),
            additionalItem = AdditionalItem()
        )

        shouldThrow<ValidationException> {
            validationService.validate(request).block()
        }.message shouldBe "Validation failed for payment method [BANK_TRANSFER]: Field 'bankDetails' is required and cannot be null."
    }

    "should pass validation for VISA with valid last4" {
        val validationRules = """
            {
                "requiredFields": ["last4"],
                "fieldTypes": { "last4": "string" }
            }
        """
        every { paymentMethodService.getValidationRules("VISA") } returns Mono.just(Json.of(validationRules))

        val request = CalculationRequest(
            customerId = "12345",
            price = BigDecimal("300.00"),
            paymentMethod = "VISA",
            datetime = LocalDateTime.parse("2024-12-18T13:00:00"),
            additionalItem = AdditionalItem(last4 = "1234")
        )

        validationService.validate(request).block() shouldBe null
    }

    "should fail validation for VISA with invalid last4" {
        val validationRules = """
            {
                "requiredFields": ["last4"],
                "fieldTypes": { "last4": "string" }
            }
        """
        every { paymentMethodService.getValidationRules("VISA") } returns Mono.just(Json.of(validationRules))

        val request = CalculationRequest(
            customerId = "12345",
            price = BigDecimal("300.00"),
            paymentMethod = "VISA",
            datetime = LocalDateTime.parse("2024-12-18T13:00:00"),
            additionalItem = AdditionalItem(last4 = null)
        )

        shouldThrow<ValidationException> {
            validationService.validate(request).block()
        }.message shouldBe "Validation failed for payment method [VISA]: Field 'last4' is required and cannot be null."
    }
})
