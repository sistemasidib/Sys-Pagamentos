package br.com.bioregistro.flowbank.service.client.Checkout;

import br.com.bioregistro.flowbank.form.Boleto.Bradesco.BoletoBradescoRequest;
import br.com.bioregistro.flowbank.form.Boleto.Bradesco.BoletoBradescoResponse;
import br.com.bioregistro.flowbank.model.PaymentResponse;
import br.com.bioregistro.flowbank.service.client.Checkout.model.request.PaymentSplitRequest;
import br.com.bioregistro.flowbank.service.client.Checkout.model.request.PessoaReq;
import br.com.bioregistro.flowbank.service.client.Checkout.model.request.ProductReq;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.ProductResp;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.RedirectURLResp;
import br.com.bioregistro.flowbank.service.client.Checkout.providers.AuthHeaderProvider;
import br.com.bioregistro.flowbank.service.client.providers.AuthRequestHeaderFactory;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "caphover-api")
@RegisterClientHeaders(AuthHeaderProvider.class)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CheckoutClient {

    @POST
    @Path("products/public")
    ProductResp criarProduto(ProductReq request);

    @POST
    @Path("products/public")
    List<ProductResp> recuperarProdutos(@QueryParam("search") String search);

    @POST
    @Path("products/{uuid}/generate-link/public")
    RedirectURLResp gerarOrdemPagamentoURI(@PathParam("uuid") String uuid);

    @POST
    @Path("transacao")
    PaymentResponse criarOrdemPagamentoCartaoSplit(PaymentSplitRequest request);

    @POST
    @Path("checkout/encrypt")
    String gerarCodigoClienteEncrypt(PessoaReq request);

}
