package br.com.bioregistro.flowbank.service.client.strategy;

import br.com.bioregistro.flowbank.model.ClientResquestPIX;
import br.com.bioregistro.flowbank.model.PixForm;
import br.com.bioregistro.flowbank.model.TypeClient;
import br.com.bioregistro.flowbank.service.client.bradesco.BradescoService;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBank;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBankResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ClientStrategyFactory {

    private final Map<TypeClient, ClientBank> strategies = new HashMap<>();

    public ClientStrategyFactory(
            BradescoService bradesco
    ) {
        strategies.put(TypeClient.BRADESCO, bradesco);
    }

    public ClientBank<ClientBankResponse, PixForm, Integer> getStrategy(ClientResquestPIX resquest) {
        return strategies.get(resquest.client());
    }

    public ClientBank getStrategy(TypeClient client) {
        return strategies.get(client);
    }

}
