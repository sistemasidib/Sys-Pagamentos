package br.com.bioregistro.flowbank.service.client.Checkout.model.enuns;

import jakarta.ws.rs.BadRequestException;

import java.util.Arrays;
import java.util.Optional;

public enum EventStatus {

    APPROVED("approved"),
    COMPLETED("completed"),
    CHARGEBACK("chargeback"),
    REFUNDED("refunded"),
    DECLINED("declined");

    private final String value;

    EventStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Optional<EventStatus> from(String status) {
        if (status == null || status.isBlank()) throw new BadRequestException("Status de pagamento inválido: vazio ou nulo.");

        String cleanStatus = status.toLowerCase().replace("payment.", "");

        return Arrays.stream(values())
                .filter(s -> cleanStatus.equals(s.getValue()))
                .findFirst();
    }


}

