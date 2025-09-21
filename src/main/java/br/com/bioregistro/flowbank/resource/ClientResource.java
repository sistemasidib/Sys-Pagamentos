package br.com.bioregistro.flowbank.resource;

import br.com.bioregistro.flowbank.model.ClientResponse;
import br.com.bioregistro.flowbank.model.ClientResquestPIX;
import br.com.bioregistro.flowbank.model.PaymentOrderForm;
import br.com.bioregistro.flowbank.model.TypeClient;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.ProductResp;
import br.com.bioregistro.flowbank.service.client.ClientService;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBankResponse;
import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

import java.net.URISyntaxException;

@Path("client")
public class ClientResource {

    @Inject
    private ClientService service;

    @POST
    @Path("payment/pix")
    public ClientBankResponse processPaymentPIX(ClientResquestPIX request, @Context HttpServerRequest serverRequest) throws URISyntaxException {
        return service.gerarLancamentoPrevistoPix(request, serverRequest);
    }


    @POST
    @Path("card/split")
    public ClientResponse gerarOrdemPagamentoCartaoSplit(PaymentOrderForm form) {
        return service.criarOrdemDePagamentoCartaoSplit(form.clientId(), TypeClient.CONJO,form.alias());
    }


}
