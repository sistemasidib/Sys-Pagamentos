package br.com.bioregistro.flowbank.resource;

import br.com.bioregistro.flowbank.service.client.Checkout.CheckoutService;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.CallbackResponse;
import br.com.bioregistro.flowbank.service.client.ClientService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("callback")
public class CallbackResource {

    @Inject
    ClientService service;

    @POST
    @Path("card/split")
    public void callBackCardSplit(CallbackResponse callbackResponse) {
        service.callbackCardSplit(callbackResponse);
    }

}
