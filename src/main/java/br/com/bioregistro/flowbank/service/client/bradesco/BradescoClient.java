package br.com.bioregistro.flowbank.service.client.bradesco;
import br.com.bioregistro.flowbank.form.Boleto.Bradesco.BoletoBradescoRequest;
import br.com.bioregistro.flowbank.form.Boleto.Bradesco.BoletoBradescoResponse;
import br.com.bioregistro.flowbank.service.client.providers.AuthRequestHeaderFactory;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "bradesco-api")
@RegisterClientHeaders(AuthRequestHeaderFactory.class)
@Path("/apipix") // ajuste para o caminho real da API
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BradescoClient {

    @POST
    BoletoBradescoResponse criarBoleto(BoletoBradescoRequest request);

    @POST
    @Path("transacao")
    BoletoBradescoResponse criarPix(BoletoBradescoRequest request);
}

