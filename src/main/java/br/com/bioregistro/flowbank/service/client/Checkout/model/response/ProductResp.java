package br.com.bioregistro.flowbank.service.client.Checkout.model.response;

import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentCompany;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentProvider;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.ProdutoExterno;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Localidade;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.MetaDataDTO;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.Tenant;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ProductResp(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        BigDecimal comparePrice,
        String image,
        String status,
        String templateCheckout,
        MetaDataDTO metadata,
        UUID publicUuid,
        int salesCount,
        BigDecimal conversionRate,
        Tenant tenant,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public ProdutoExterno toEntity(PaymentCompany company, String clientProduct, BigDecimal originalPrice) {
        ProdutoExterno entity = new ProdutoExterno();
        entity.externalProdutoId = id.toString();
        entity.createdAt = createdAt.toLocalDateTime();
        entity.clientIdReference = clientProduct;
        entity.company = company;
        entity.amount = originalPrice;
        entity.provider = PaymentProvider.findById(1);

        return entity;
    }

}
