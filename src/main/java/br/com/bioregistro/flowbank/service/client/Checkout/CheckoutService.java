package br.com.bioregistro.flowbank.service.client.Checkout;

import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentCompany;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentProvider;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentTransaction;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.ProdutoExterno;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.*;
import br.com.bioregistro.flowbank.model.TypeOperation;
import br.com.bioregistro.flowbank.service.client.Checkout.model.enuns.TaxType;
import br.com.bioregistro.flowbank.service.client.Checkout.model.request.PessoaReq;
import br.com.bioregistro.flowbank.service.client.Checkout.model.request.ProductReq;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.CheckoutResponse;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.ProductResp;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.RedirectURLResp;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBank;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@ApplicationScoped
public class CheckoutService implements ClientBank<CheckoutResponse, Long, Integer> {

    @RestClient
    CheckoutClient checkoutClient;

    public CheckoutService(@RestClient CheckoutClient checkoutClient) {
        this.checkoutClient = checkoutClient;
    }

    public void generateOrder(Long insId, Long providerId) {

    }

    @Override
    public CheckoutResponse processOperationPIX(Inscricao candidato, TypeOperation operation, HttpServerRequest serverRequest) throws URISyntaxException {
        return null;
    }

    @Override
    public CheckoutResponse processOperationBoleto(Integer inscricao, TypeOperation operation) {
        return null;
    }

    @Override
    public CheckoutResponse processOperationPIX(Inscricao candidato, TypeOperation operation, HttpServerRequest serverRequest, Function<Long, Integer> mapper) throws URISyntaxException {
        return null;
    }

    @Override
    public String gerarOrdemDepagamentoCartaoSplit(String clientId, String alias) {

        Inscricao inscricao = Inscricao.findById(clientId);

        Cargo cargo = inscricao.localidade.cargo;

        Localidade localidade = inscricao.localidade;

        Candidato candidato = inscricao.candidato;

        ProdutoExterno prod = save(localidade, alias);

        RedirectURLResp url = checkoutClient.gerarOrdemPagamentoURI(prod.externalProdutoId);

        String clientCode = checkoutClient.gerarCodigoClienteEncrypt(new PessoaReq(
                candidato.canNome,candidato.canCPF,candidato.canTelefone1,candidato.canEmail
        ));

        return  url.checkoutUrl() + "?uid=" + clientCode;
    }

    @Transactional
    public ProdutoExterno save(Localidade localidade, String alias) {

        Optional<ProdutoExterno> prodct = ProdutoExterno.find("clientIdReference = ?1 and company.alias = ?2", localidade.locId, alias).firstResultOptional();

        return prodct.orElseGet(() -> {
            ProductResp resp = checkoutClient.criarProduto(
                    new ProductReq(localidade.cargo.carDescricao, localidade.cargo.carDescricao, localidade.cargo.carVlInscricao, BigDecimal.valueOf(1), TaxType.FIXED.getDescription())
            );

            Optional<PaymentCompany> company = PaymentCompany.find("alias = ?1", alias).firstResultOptional();

            ProdutoExterno prod = resp.toEntity(company.orElseThrow(() -> new RuntimeException("Company Inválida")), localidade.locId.toString(), localidade.cargo.carVlInscricao);

            prod.persistAndFlush();

            return prod;
        });
    }
}
