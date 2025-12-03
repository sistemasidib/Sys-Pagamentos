package br.com.bioregistro.flowbank.service.client;

import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentProvider;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentTransaction;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.ProdutoExterno;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.enuns.PaymentTransactionType;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Cargo;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Localidade;
import br.com.bioregistro.flowbank.model.ClientResquestPIX;
import br.com.bioregistro.flowbank.model.PixForm;
import br.com.bioregistro.flowbank.service.PixService;
import br.com.bioregistro.flowbank.service.client.Checkout.CheckoutService;
import br.com.bioregistro.flowbank.service.client.Checkout.model.enuns.PaymentStatus;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.CallbackResponse;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.ProductResp;
import br.com.bioregistro.flowbank.service.client.strategy.ClientStrategyFactory;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBankResponse;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Context;
import br.com.bioregistro.flowbank.model.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@ApplicationScoped
public class ClientService {

    private final ClientStrategyFactory clientStrategyFactory;
    private final PixService pixService;
    private final CheckoutService checkoutService;

    @ConfigProperty(name = "app.security.api-ip")
    private String ipKey;

    @ConfigProperty(name = "app.security.api-X-Tenant-ID")
    private String apiXTenantID;

    @ConfigProperty(name = "app.security.api-X-Webhook-Signature")
    private String apiXWebhookSignature;



    private static final Logger log = Logger.getLogger(ClientService.class);

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




    public void callbackCardSplit(CallbackResponse response, HttpHeaders headers,
                                   UriInfo uriInfo,
                                   HttpServerRequest request) {

        validateSignuture(request, headers);

        Optional<ProdutoExterno> prod = ProdutoExterno.find("externalProdutoId = ?1", response.productId()).firstResultOptional();

        prod.ifPresentOrElse(
                it -> {
                    PaymentTransaction transaction =  salvarTransacao(response, it);




                    if(it.company.alias.equals("IDC")) {

                        AtualizarDadosIdecan(response,transaction,it.clientIdReference);
                    } else {
                        AtualizarDadosIdib(response,transaction,it.clientIdReference);
                    }

                },
                () -> {
                    throw new BadRequestException("Erro ao buscar produto");
                });

    }


    @Transactional
    public PaymentTransaction salvarTransacao(CallbackResponse response, ProdutoExterno product){

        Optional<PaymentTransaction> transactionOpt =
                PaymentTransaction.find("externalId = ?1", response.transactionId())
                        .firstResultOptional();

        PaymentTransaction transaction = transactionOpt.orElse(new PaymentTransaction());

        transaction.externalId = response.transactionId();
        transaction.product = product;
        transaction.currency = response.currency();
        transaction.amount = response.amount();
        transaction.status = response.status();
        transaction.paymentMethod = response.paymentMethod();
        transaction.clientReferenceId = response.customerDocument();
        transaction.createdAt = response.timestamp().atZoneSameInstant(ZoneId.of("America/Sao_Paulo")).toLocalDateTime();
        transaction.persist();

        return transaction;
    }


    @Transactional
    public void AtualizarDadosIdecan(CallbackResponse response, PaymentTransaction transaction, String localidadeId) {

        Inscricao inscricao = Inscricao.find(
                "candidato.canCPF = ?1  and localidade.locId = ?2",
                response.customerDocument(),transaction.product.clientIdReference
        ).firstResult();

        PaymentStatus paymentStatus = PaymentStatus.from(response.status()).orElseThrow(() -> new BadRequestException("Status inválido: " + response.status()));

        if (paymentStatus.isApproved()) {
            inscricao.insDtPagamento = response.timestamp()
                    .atZoneSameInstant(ZoneId.of("America/Sao_Paulo"))
                    .toLocalDateTime();
        } else if (paymentStatus.isRefunded()) {
            inscricao.insDtPagamento = null;
        }

        inscricao.persist();
    }

    @Transactional
    public void AtualizarDadosIdib(CallbackResponse response, PaymentTransaction transaction, String localidadeId) {

        br.com.bio.registro.core.runtime.entities.idib.dbo.Inscricao inscricao = br.com.bio.registro.core.runtime.entities.idib.dbo.Inscricao.find(
                "candidato.canCPF = ?1  and localidade.locId = ?2",
                response.customerDocument(),transaction.product.clientIdReference
        ).firstResult();

        PaymentStatus paymentStatus = PaymentStatus.from(response.status()).orElseThrow(() -> new BadRequestException("Status inválido: " + response.status()));

        if (paymentStatus.isApproved()) {
            inscricao.insDtPagamento = response.timestamp()
                    .atZoneSameInstant(ZoneId.of("America/Sao_Paulo"))
                    .toLocalDateTime();
        } else if (paymentStatus.isRefunded()) {
            inscricao.insDtPagamento = null;
        }

        inscricao.persist();
    }



    public void validarIp(HttpServerRequest request, HttpHeaders headers) {

        String xff = headers.getHeaderString("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            xff = xff.split(",")[0].trim();
        } else {
            xff = request.remoteAddress().host();
        }

        log.infof("Verificando callback do IP: %s", xff);

        if(!ipKey.equals(xff)) {
            throw new ForbiddenException("IP não autorizado");
        }
    }

    public void validateSignuture(HttpServerRequest request, HttpHeaders headers) {
        String xTenantID = headers.getHeaderString("X-Tenant-ID");
        String xWebhooksignature = headers.getHeaderString("X-Webhook-Signature");

        log.infof("Verificando assinatura do hook: %s e id: %s", xWebhooksignature, xTenantID);

        if (!Objects.equals(xTenantID, apiXTenantID) || !Objects.equals(xWebhooksignature, apiXWebhookSignature)) {
            throw new ForbiddenException("Assinatura ou ID do tenant inválidos");
        }

    }

}
