package br.com.bioregistro.flowbank.service.client.Checkout;

import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentCompany;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.ProdutoExterno;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.*;
import br.com.bioregistro.flowbank.form.Boleto.Bradesco.FormOrder;
import br.com.bioregistro.flowbank.model.PaymentOrderForm;
import br.com.bioregistro.flowbank.model.TypeOperation;
import br.com.bioregistro.flowbank.service.client.Checkout.model.enuns.TaxType;
import br.com.bioregistro.flowbank.service.client.Checkout.model.request.BranchIdReq;
import br.com.bioregistro.flowbank.service.client.Checkout.model.request.PessoaReq;
import br.com.bioregistro.flowbank.service.client.Checkout.model.request.PriceFee;
import br.com.bioregistro.flowbank.service.client.Checkout.model.request.ProductReq;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.CheckoutResponse;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.ProductResp;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.RedirectURLResp;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBank;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

        FormOrder form =  FormOrder.from(clientId, alias);

        ProdutoExterno prod = save(form, alias);

        RedirectURLResp url = checkoutClient.gerarOrdemPagamentoURI(prod.externalProdutoId);

        String clientCode = checkoutClient.gerarCodigoClienteEncrypt(new PessoaReq(
                form.canNome(),form.canCpf(),form.canTelefone(),form.canEmail()
        ));

        return  url.checkoutUrl() + "?uid=" + clientCode;
    }

    @Transactional
    public ProductResp atualizarProdutoComExternalId(String uuid, String branchId) {
        BranchIdReq request = new BranchIdReq(branchId);
        return checkoutClient.atualizarExternalIdProduto(uuid, request);
    }


    @Transactional
    public ProdutoExterno save(FormOrder form, String alias) {

        Optional<ProdutoExterno> prodct = ProdutoExterno.find("clientIdReference = ?1 and company.alias = ?2", form.localidadeId(), alias).firstResultOptional();
        //TODO AJUSTAR PARA TAXA SER AUTOMATICA E DE FORMA DINAMICA
        return prodct.orElseGet(() -> {

            Optional<PaymentCompany> company = PaymentCompany.find("alias = ?1", alias).firstResultOptional();
            PaymentCompany comp = company.orElseThrow(() -> new RuntimeException("Company Inválida"));

            ProductResp resp = checkoutClient.criarProduto(
                    new ProductReq(form.cargoDesc(), form.carVlInscricao(), comp.companyId.toString() ,  Map.of(
                            "edital_id", form.editalId(),
                            "edital_nome", form.editalId()
                    ), List.of(new PriceFee(3, TaxType.PERCENTAGE.getDescription(), BigDecimal.valueOf(12)))));


            ProdutoExterno prod = resp.toEntity(comp, form.localidadeId(), form.carVlInscricao());

            prod.persist();

            return prod;
        });
    }





}
