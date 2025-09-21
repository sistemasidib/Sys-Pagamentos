package br.com.bioregistro.flowbank.service.client;

import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentTransaction;
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

        PaymentTransaction paymentTransaction = new PaymentTransaction();
        String urlResponse = clientStrategyFactory.getStrategy(type).gerarOrdemDepagamentoCartaoSplit(clientId, alias, paymentTransaction);


        paymentTransaction.paymentMethod = PaymentTransactionType.CARTAO.toString();
        paymentTransaction.clientReferenceId = clientId;
        pesistPayment(paymentTransaction);

        return new ClientResponse(urlResponse);
    }

    @Transactional
    public void pesistPayment(PaymentTransaction paymentTransaction) {
        paymentTransaction.persist();
    }

    @Transactional
    public void callbackCardSplit(CallbackResponse response) {
        Optional<PaymentTransaction> transaction = PaymentTransaction.find("external_id = ?1", response.transactionId()).firstResultOptional();

        transaction.ifPresentOrElse(
                it -> {
                    it.status = response.status();
                    it.persist();
                },
                () -> {
                    throw new RuntimeException("Erro ao buscar Transação");
                });
    }

}
