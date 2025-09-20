package br.com.bioregistro.flowbank.service.client.Checkout;

import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentCompany;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Candidato;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Cargo;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Edital;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import br.com.bioregistro.flowbank.model.TypeOperation;
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
import java.util.function.Function;

@ApplicationScoped
public class CheckoutService implements ClientBank<CheckoutResponse, Long, Integer> {

    @RestClient
    CheckoutClient checkoutClient;

    public CheckoutService(@RestClient CheckoutClient checkoutClient) {
        this.checkoutClient = checkoutClient;
    }

    public void generateProduct(Long cargoId, Long editalId) {
        Cargo  cargo = Cargo.findById(cargoId);
        Edital edital = Edital.findById(editalId);

        new ProductReq(
                edital.ediNomeConcurso,
                edital.ediEntidade,
                cargo.carVlInscricao
        );

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
    public String gerarOrdemDepagamentoCartaoSplit(Long clientId, Long companyId) {

        Inscricao inscricao = Inscricao.findById(clientId);
        System.out.println(PaymentCompany.findAll().count() + " Teste");
        PaymentCompany company = PaymentCompany.findById(1);
        Cargo cargo = inscricao.localidade.cargo;
        Candidato candidato = inscricao.candidato;

        String prodId = save(company, cargo, companyId);

        RedirectURLResp url = checkoutClient.gerarOrdemPagamentoURI(prodId);

        String clientCode = checkoutClient.gerarCodigoClienteEncrypt(new PessoaReq(
                candidato.canNome,candidato.canCPF,candidato.canTelefone1,candidato.canEmail
        ));

        return  url.checkoutUrl() + "?uid=" + clientCode;
    }

    @Transactional
    public String save(PaymentCompany company, Cargo cargo, Long companyId) {
        String prodId = company.produtos.stream()
                .filter(p -> p.clientIdReference.equals(cargo.carId.toString()))
                .map(p -> p.externalProdutoId)
                .findFirst()
                .orElseGet(() -> {
                    ProductResp resp = checkoutClient.criarProduto(
                            new ProductReq(cargo.carDescricao, cargo.carDescricao, cargo.carVlInscricao)
                    );

                    resp.toEntity(company, cargo.carId.toString()).persist();

                    return resp.id().toString();
                });
        return prodId;
    }
}
