package br.com.bioregistro.flowbank.resource;

import br.com.bioregistro.flowbank.service.client.Checkout.CheckoutService;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.CallbackResponse;
import br.com.bioregistro.flowbank.service.client.ClientService;
import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.UriInfo;

@Path("callback")
public class CallbackResource {

    @Inject
    ClientService service;

    @POST
    @Path("card/split")
    public void callBackCardSplit(CallbackResponse callbackResponse,
                                  @Context HttpHeaders headers,
                                  @Context UriInfo uriInfo,
                                  @Context HttpServerRequest request) {
        service.callbackCardSplit(callbackResponse, headers, uriInfo, request);
    }

}
