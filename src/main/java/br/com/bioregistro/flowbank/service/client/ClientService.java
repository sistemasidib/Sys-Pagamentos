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
import br.com.bioregistro.flowbank.model.*;
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

    public ClientBankResponse gerarLancamentoPrevistoPix(ClientResquestPIX clientResquestPIX, HttpServerRequest serverRequest) throws URISyntaxException {

        Inscricao cand = Inscricao.<Inscricao>find("insId = ?1", clientResquestPIX.inscricao())
                .firstResultOptional()
                .orElseThrow(() -> new RuntimeException("Erro ao buscar Inscricao"));


        return clientStrategyFactory
                .getStrategy(clientResquestPIX)
                .processOperationPIX(cand, clientResquestPIX.operation(), serverRequest, pixService::createPix);
    };

    public void criarNovoProdutoClient(Long produtoId) {

    }

    public void criarOrdemDePagamentoCartaoSplit(Long clientId, Long produtoId, TypeClient type) {
        clientStrategyFactory.getStrategy(type).gerarOrdemDepagamentoCartaoSplit(clientId, produtoId);
    }

}
