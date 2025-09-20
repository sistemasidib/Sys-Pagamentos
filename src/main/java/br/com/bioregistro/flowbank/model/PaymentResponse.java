package br.com.bioregistro.flowbank.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PaymentResponse(
        String event,
        String transactionId,
        String productId,
        BigDecimal amount,
        String currency,
        String status,
        String paymentMethod,
        String customerDocument,
        OffsetDateTime timestamp
) {}
