package br.com.bioregistro.flowbank.service.client.strategy;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.ConcursoBancoLogin;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import br.com.bioregistro.flowbank.model.ClientResquestPIX;
import br.com.bioregistro.flowbank.model.PixForm;
import br.com.bioregistro.flowbank.model.enuns.TypeClient;
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

    public ClientBankResponse aplicarBaixaPagamentoBoleto(Integer inscricao, TypeOperation operation,HttpServerRequest serverRequest,Function<Inscricao, Long> mapper,Long editalId) {
//faz o schedule procurar por editais que tão open e com pagamento ativo ;D
        for (TypeClient typeClient : strategies.keySet()) {
            ConcursoBancoLogin concursoBancoLogin = new ConcursoBancoLogin();
            concursoBancoLogin.find("banId", 18);

            strategies.get(typeClient).baixaBoleto( serverRequest, null ,editalId, concursoBancoLogin);
        }

        return null;

    }


//    public ClientBankResponse executePixOp(ClientResquestPIX resquest, Inscricao ins, br.com.bioregistro.flowbank.model.enuns.TypeOperation operation, Function<PixForm, Integer> lamb, HttpServerRequest serverRequest) throws URISyntaxException {
//        ClientBank<ClientBankResponse, PixForm, Integer> clientBank = strategies.get(resquest.client());
//        return clientBank.processOperationPIX(ins, operation, serverRequest, lamb);
//    }

}
