package br.com.bioregistro.flowbank.service.client.Checkout.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record CallbackResponse(
        String event,
        String transactionId,
        String productId,
        BigDecimal amount,
        String currency,
        String status,
        String paymentMethod,
        String customerDocument,
        OffsetDateTime timestamp
) {
}
