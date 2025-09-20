package br.com.bioregistro.flowbank.service.client.Checkout.model.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Tenant(
        UUID id,
        String name,
        String domain,
        String subdomain,
        String logo,
        Settings settings,
        Theme theme,
        boolean isActive,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

}