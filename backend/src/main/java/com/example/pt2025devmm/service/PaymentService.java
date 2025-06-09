package com.example.pt2025devmm.service;

import com.example.pt2025devmm.dto.PaymentRequestDto;
import com.example.pt2025devmm.dto.PaymentResponseDto;
import com.example.pt2025devmm.validator.PaymentValidator;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentValidator validator;

    public PaymentService(PaymentValidator validator) {
        this.validator = validator;
    }

    public PaymentResponseDto processPayment(PaymentRequestDto dto) {
        // Validate input
        validator.validate(dto);

        // Detect card brand
        String cardBrand = detectCardBrand(dto.getCardNumber());

        // Mask card number for response
        String maskedCardNumber = maskCardNumber(dto.getCardNumber());

        // Create response data object
        PaymentDetailsDto responseData = new PaymentDetailsDto(
                cardBrand,
                maskedCardNumber,
                dto.getInvoiceName());

        return new PaymentResponseDto(200, "Payment processed successfully", responseData);
    }

    private String detectCardBrand(String cardNumber) {
        if (cardNumber.startsWith("4")) {
            return "Visa";
        } else if (cardNumber.startsWith("5")) {
            return "MasterCard";
        }
        return "Unknown";
    }

    private String maskCardNumber(String cardNumber) {
        return "****-****-****-" + cardNumber.substring(12);
    }

    // Internal DTO class for payment details
    private static class PaymentDetailsDto {
        public final String cardBrand;
        public final String maskedCardNumber;
        public final String invoiceName;

        public PaymentDetailsDto(String cardBrand, String maskedCardNumber, String invoiceName) {
            this.cardBrand = cardBrand;
            this.maskedCardNumber = maskedCardNumber;
            this.invoiceName = invoiceName;
        }
    }
}
