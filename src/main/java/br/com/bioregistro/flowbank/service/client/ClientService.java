package br.com.bioregistro.flowbank.service.client;

import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentProvider;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentTransaction;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.ProdutoExterno;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.enuns.PaymentTransactionType;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import br.com.bioregistro.flowbank.model.ClientResquestPIX;
import br.com.bioregistro.flowbank.model.PixForm;
import br.com.bioregistro.flowbank.service.PixService;
import br.com.bioregistro.flowbank.service.client.Checkout.CheckoutService;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.CallbackResponse;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.ProductResp;
import br.com.bioregistro.flowbank.service.client.strategy.ClientStrategyFactory;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBankResponse;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import br.com.bioregistro.flowbank.model.*;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ClientService {

    private final ClientStrategyFactory clientStrategyFactory;
    private final PixService pixService;
    private final CheckoutService checkoutService;

    public ClientService(ClientStrategyFactory clientStrategyFactory,PixService pixService, CheckoutService checkoutService) {
        this.clientStrategyFactory = clientStrategyFactory;
        this.pixService = pixService;
        this.checkoutService = checkoutService;
    }

    public ClientBankResponse gerarLancamentoPrevistoPix(ClientResquestPIX clientResquestPIX, HttpServerRequest serverRequest) throws URISyntaxException {

        Inscricao cand = Inscricao.<Inscricao>find("insId = ?1", clientResquestPIX.inscricao())
                .firstResultOptional()
                .orElseThrow(() -> new RuntimeException("Erro ao buscar Inscricao"));


        return clientStrategyFactory
                .getStrategy(clientResquestPIX)
                .processOperationPIX(cand, clientResquestPIX.operation(), serverRequest, pixService::createPix);
    };


    public ClientResponse criarOrdemDePagamentoCartaoSplit(String clientId, TypeClient type, String alias) {

        String urlResponse = clientStrategyFactory.getStrategy(type).gerarOrdemDepagamentoCartaoSplit(clientId, alias);

        return new ClientResponse(urlResponse);
    }

    @Transactional
    public void pesistPayment(PaymentTransaction paymentTransaction) {
        paymentTransaction.persist();
    }

    @Transactional
    public void callbackCardSplit(CallbackResponse response) {
        PaymentTransaction transaction = new PaymentTransaction();

        Optional<ProdutoExterno> prod = ProdutoExterno.find("external_id = ?1", response.productId()).firstResultOptional();

        prod.ifPresentOrElse(
                it -> {
                    transaction.externalId = response.transactionId();
                    transaction.product = it;
                    transaction.currency = response.currency();
                    transaction.amount = response.amount();
                    transaction.status = response.paymentMethod();
                    transaction.paymentMethod = response.paymentMethod();
                    transaction.clientReferenceId = response.customerDocument();
                    transaction.persist();
                },
                () -> {
                    throw new RuntimeException("Erro ao buscar produto");
                });



    }

}
