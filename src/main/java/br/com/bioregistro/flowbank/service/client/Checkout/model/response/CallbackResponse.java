package br.com.bioregistro.flowbank.service.client.Checkout.model.response;

import java.math.BigDecimal;

public record CallbackResponse(
        String event,
        String transactionId,
        String productId,
        BigDecimal amount,
        String currency,
        String status,
        String paymentMethod,
        String customerDocument,
        String timestamp
) {
}
