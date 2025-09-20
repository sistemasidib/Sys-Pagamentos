package br.com.bioregistro.flowbank.service.client.Checkout.model.response;

import java.util.UUID;

public record MetaDataDTO(
        UUID publicUuid,
        String createdBy
) {}