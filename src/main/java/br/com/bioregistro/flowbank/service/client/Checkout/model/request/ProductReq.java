package br.com.bioregistro.flowbank.service.client.Checkout.model.request;

import java.math.BigDecimal;

public record ProductReq(
        String name,
        String description,
        BigDecimal price,
        String status,
        String template_checkout
) {

    public ProductReq(String name,
                      String description,
                      BigDecimal price) {
        this(name, description, price, "ATIVO", "clean");
    }

}
