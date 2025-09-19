package br.com.bioregistro.flowbank.service.client.Checkout.model.request;

public record ProductReq(
        String name,
        String description,
        Long price,
        String status
) {

    public ProductReq(String name,
                      String description,
                      Long price) {
        this(name, description, price, "ATIVO");
    }

}
