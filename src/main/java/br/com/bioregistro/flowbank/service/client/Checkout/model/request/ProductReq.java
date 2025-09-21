package br.com.bioregistro.flowbank.service.client.Checkout.model.request;

import java.math.BigDecimal;

public record ProductReq(
        String name,
        String description,
        BigDecimal priceOriginal,
        BigDecimal taxValue,
        String taxType,
        String status,
        String template_checkout
) {

    public ProductReq(String name,
                      String description,
                      BigDecimal price,
                      BigDecimal taxValue,
                      String taxType
    ) {
        this(name, description, price, taxValue, taxType ,"ATIVO", "clean");
    }

}
