package br.com.bioregistro.flowbank.service.client.Checkout.model.response;

public record RedirectURLResp(
        String checkoutUrl,
        String publicUuid
) {
}
