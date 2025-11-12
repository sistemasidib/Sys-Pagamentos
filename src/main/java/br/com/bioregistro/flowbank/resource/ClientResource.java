package br.com.bioregistro.flowbank.resource;

import br.com.bioregistro.flowbank.model.ClientResquestPIX;
import br.com.bioregistro.flowbank.service.client.BB.BbTokenService;
import br.com.bioregistro.flowbank.service.client.ClientService;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBankResponse;
import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

import java.net.URISyntaxException;

@Path("client")
public class ClientResource {

    @Inject
    BbTokenService bbTokenService;
    @Inject
    private ClientService service;

    private String urlAuthBB;

    @POST
    @Path("payment/pix")
    public ClientBankResponse processPaymentPIX(ClientResquestPIX request, @Context HttpServerRequest serverRequest) throws URISyntaxException {
      //  return service.gerarLancamentoPrevistoPix(request, serverRequest);
        return null;
    }

    @POST
    @Path("payment/baixa")
    public ClientBankResponse processBaixaPayment(ClientResquestPIX request, @Context HttpServerRequest serverRequest) throws URISyntaxException {

        return null;

            //    gerarLancamentoPrevistoPix(request, serverRequest);
    }

//    @GET
//    @Path("/bb/token")
//    public Response tokenBB(@Context HttpServerRequest req) {
//        String bearer = bbTokenService.getTokenBancoBrasil(login, urlAuthBB);
//        return Response.ok("Token ok").build(); // não exponha o token em produção
//    }



}
