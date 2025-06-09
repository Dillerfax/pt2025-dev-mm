package com.example.pt2025devmm.controller;

import com.example.pt2025devmm.dto.PaymentRequestDto;
import com.example.pt2025devmm.dto.PaymentResponseDto;
import com.example.pt2025devmm.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDto> processPayment(@RequestBody PaymentRequestDto dto) {
        PaymentResponseDto response = paymentService.processPayment(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}