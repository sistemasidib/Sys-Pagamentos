package br.com.bioregistro.flowbank.service.bradesco;

import br.com.bioregistro.flowbank.client.BradescoClient;
import br.com.bioregistro.flowbank.form.Boleto.Bradesco.BoletoBradescoRequest;
import br.com.bioregistro.flowbank.form.Boleto.Bradesco.BoletoBradescoResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BradescoService {




    @RestClient
    private BradescoClient client;

    public BradescoService(BradescoClient client) {
        this.client = client;
    }

    public BoletoBradescoResponse criar(BoletoBradescoRequest request) {
        return client.criarBoleto(request);
    }
}

