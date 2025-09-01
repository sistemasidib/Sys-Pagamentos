package br.com.bioregistro.flowbank.client;
import br.com.bioregistro.flowbank.form.Boleto.Bradesco.BoletoBradescoRequest;
import br.com.bioregistro.flowbank.form.Boleto.Bradesco.BoletoBradescoResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "bradesco-api")
@Path("/pagamentos") // ajuste para o caminho real da API
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BradescoClient {

    @POST
    BoletoBradescoResponse criarBoleto(BoletoBradescoRequest request);
}

