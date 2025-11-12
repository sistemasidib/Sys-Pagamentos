package br.com.bioregistro.flowbank.service.client.strategy;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.ConcursoBancoLogin;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import br.com.bioregistro.flowbank.model.ClientResquestPIX;
import br.com.bioregistro.flowbank.model.PixForm;
import br.com.bioregistro.flowbank.model.TypeClient;

import br.com.bioregistro.flowbank.model.TypeOperation;
import br.com.bioregistro.flowbank.service.client.BB.BancoBrasilService;
import br.com.bioregistro.flowbank.service.client.bradesco.BradescoService;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBank;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBankResponse;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@ApplicationScoped
public class ClientStrategyFactory {

    //pq q é um client bank, pq nossos bancos são client bank!
    private final Map<TypeClient, ClientBank> strategies = new HashMap<>();

    private final BancoBrasilService bancoBrasilService;

    public ClientStrategyFactory(
            BradescoService bradesco,
            BancoBrasilService bancoBrasilService
    ) {
        strategies.put(TypeClient.BRADESCO, bradesco);
        strategies.put(TypeClient.BANCOBRASIL, bancoBrasilService);
        this.bancoBrasilService = bancoBrasilService;
    }

    public ClientBank getStrategy(ClientResquestPIX resquest) {
        return strategies.get(resquest.client());
    }


    //preciso de um metodo que pra cada tipo de cliente, vai rodar a operação de baixa a mlk

    public void baixaParaTodosOsBancosDoEdital(Integer editalId, HttpServerRequest serverRequest) {
        for (Map.Entry<TypeClient, ClientBank> entry : strategies.entrySet()) {
            TypeClient client = entry.getKey();
            ClientBank clientBank = entry.getValue();


            // Panache: consulta estática na entidade
            Optional<ConcursoBancoLogin> optLogin =
                    ConcursoBancoLogin.find("concursoId = ?1 ",
                            editalId.intValue()).firstResultOptional();

            if (optLogin.isEmpty()) {
                System.out.printf("[BAIXA] Sem credenciais para edital=%d, (%s)%n",
                        editalId, client);
                continue;
            }

            ConcursoBancoLogin login = optLogin.get();
            try {
                clientBank.baixaBoleto(serverRequest, editalId, login);
                System.out.printf("[BAIXA] OK edital=%d, (%s)%n",
                        editalId, client);
            } catch (Exception ex) {
                System.out.printf("[BAIXA] ERRO edital=%d, (%s): %s%n",
                        editalId, client, ex.getMessage());
            }
        }
    }


//    public ClientBankResponse executePixOp(ClientResquestPIX resquest, Inscricao ins, br.com.bioregistro.flowbank.model.enuns.TypeOperation operation, Function<PixForm, Integer> lamb, HttpServerRequest serverRequest) throws URISyntaxException {
//        ClientBank<ClientBankResponse, PixForm, Integer> clientBank = strategies.get(resquest.client());
//        return clientBank.processOperationPIX(ins, operation, serverRequest, lamb);
//    }

}
