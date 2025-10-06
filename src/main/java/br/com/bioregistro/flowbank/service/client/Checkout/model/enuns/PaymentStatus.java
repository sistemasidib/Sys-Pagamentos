package br.com.bioregistro.flowbank.service.client.Checkout.model.enuns;

import jakarta.ws.rs.BadRequestException;

import java.util.Arrays;
import java.util.Optional;

public enum PaymentStatus {

    APPROVED("approved"),
    COMPLETED("completed"),
    CHARGEBACK("chargeback"),
    REFUNDED("refunded");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    public static Optional<PaymentStatus> from(String status) {
        if (status == null || status.isBlank()) throw new BadRequestException("Status de pagamento inválido: vazio ou nulo.");

        String cleanStatus = status.toLowerCase().replace("payment.", "");

        return Arrays.stream(values())
                .filter(s -> cleanStatus.equals(s.getValue()))
                .findFirst();
    }


    public boolean isApproved() {
        return this == APPROVED || this == COMPLETED;
    }


    public boolean isRefunded() {
        return this == CHARGEBACK || this == REFUNDED;
    }
}