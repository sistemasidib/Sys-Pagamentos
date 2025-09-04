package br.com.bioregistro.flowbank.service.client.strategy;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import br.com.bioregistro.flowbank.model.ClientResquestPIX;
import br.com.bioregistro.flowbank.model.PixForm;
import br.com.bioregistro.flowbank.model.enuns.TypeClient;
import br.com.bioregistro.flowbank.service.client.BB.BancoBrasilService;
import br.com.bioregistro.flowbank.service.client.bradesco.BradescoService;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBank;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBankResponse;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@ApplicationScoped
public class ClientStrategyFactory {

    private final Map<TypeClient, ClientBank> strategies = new HashMap<>();

    public ClientStrategyFactory(
            BradescoService bradesco,
            BancoBrasilService bancoBrasilService
    ) {
        strategies.put(TypeClient.BRADESCO, bradesco);
        strategies.put(TypeClient.BANCOBRASIL, bancoBrasilService);
    }

    public ClientBank getStrategy(ClientResquestPIX resquest) {
        return strategies.get(resquest.client());
    }

    public ClientBankResponse executePixOp(ClientResquestPIX resquest, Inscricao ins, br.com.bioregistro.flowbank.model.enuns.TypeOperation operation, Function<PixForm, Integer> lamb, HttpServerRequest serverRequest) throws URISyntaxException {
        ClientBank<ClientBankResponse, PixForm, Integer> clientBank = strategies.get(resquest.client());
        return clientBank.processOperationPIX(ins, operation, serverRequest, lamb);
    }

}
