package com.example.pt2025devmm.validator;

import com.example.pt2025devmm.dto.PaymentRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Component
public class PaymentValidator {

    private final Validator validator;

    public PaymentValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(PaymentRequestDto dto) {
        Set<ConstraintViolation<PaymentRequestDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.iterator().next().getMessage());
        }

        // Custom CPF validation (simplified)
        String cpf = dto.getCpf();
        if (!isValidCpf(cpf)) {
            throw new IllegalArgumentException("Invalid CPF");
        }

        // Age validation (18+)
        LocalDate birthDate = dto.getBirthDate();
        if (Period.between(birthDate, LocalDate.now()).getYears() < 18) {
            throw new IllegalArgumentException("Cardholder must be at least 18 years old");
        }

        // Card expiration validation
        String expirationDate = dto.getExpirationDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        LocalDate expDate = LocalDate.parse("01/" + expirationDate, DateTimeFormatter.ofPattern("dd/MM/yy"));
        if (expDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Card has expired");
        }
    }

    private boolean isValidCpf(String cpf) {
        // Simplified CPF validation (11 digits, not all equal)
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        return true;
    }
}