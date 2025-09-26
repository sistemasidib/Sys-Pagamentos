package br.com.bioregistro.flowbank.service.client;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import br.com.bioregistro.flowbank.model.ClientResquestPIX;
import br.com.bioregistro.flowbank.model.PixForm;
import br.com.bioregistro.flowbank.service.PixService;
import br.com.bioregistro.flowbank.service.client.strategy.ClientStrategyFactory;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBankResponse;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;

import java.net.URISyntaxException;
import java.util.Optional;

@ApplicationScoped
public class ClientService {

    private final ClientStrategyFactory clientStrategyFactory;
    private final PixService pixService;

    public ClientService(ClientStrategyFactory clientStrategyFactory,PixService pixService) {
        this.clientStrategyFactory = clientStrategyFactory;
        this.pixService = pixService;
    }

//    public ClientBankResponse gerarLancamentoPrevistoPix(ClientResquestPIX clientResquestPIX, HttpServerRequest serverRequest) throws URISyntaxException {
//
//        Inscricao cand = Inscricao.<Inscricao>find("insId = ?1", clientResquestPIX.inscricao())
//                .firstResultOptional()
//                .orElseThrow(() -> new RuntimeException("Erro ao buscar Inscricao"));
//
//
//        return clientStrategyFactory.executePixOp(clientResquestPIX,cand, clientResquestPIX.operation(), serverRequest, teste -> pixService.createPix(teste));
//    };
//
//    public void aplicarBaixaPagamentoBoleto() {
//        br.com.bioregistro.flowbank.model.enuns.TypeClient.values().forEach(typeClient -> {
//            clientStrategyFactory.getStrategy(typeClient).aplicarBaixaPagamentoBoleto();
//        });
//    }

}
