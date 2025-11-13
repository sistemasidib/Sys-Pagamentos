package br.com.bioregistro.flowbank.service.client.Checkout.model.request;

import java.math.BigDecimal;

public record PriceFee(
        Integer installments,
        String feeType,
        BigDecimal feeValue
) {}