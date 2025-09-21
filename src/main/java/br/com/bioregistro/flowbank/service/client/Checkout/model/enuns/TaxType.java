package br.com.bioregistro.flowbank.service.client.Checkout.model.enuns;

public enum TaxType {
    PERCENTAGE("percentage"),
    FIXED("fixed"),;

    private final String description;

    TaxType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
