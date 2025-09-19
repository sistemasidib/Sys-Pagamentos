package br.com.bioregistro.flowbank.service.client.Checkout;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.Cargo;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Edital;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import br.com.bioregistro.flowbank.model.TypeOperation;
import br.com.bioregistro.flowbank.service.client.Checkout.model.request.ProductReq;
import br.com.bioregistro.flowbank.service.client.Checkout.model.response.CheckoutResponse;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBank;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.function.Function;

@ApplicationScoped
public class CheckoutService implements ClientBank<CheckoutResponse, Long, Integer> {


    public void generateProduct(Long cargoId, Long editalId) {
        Cargo  cargo = Cargo.findById(cargoId);
        Edital edital = Edital.findById(editalId);

        new ProductReq(
                edital.ediNomeConcurso,
                edital.ediEntidade,
                cargo.getVlInscricaoCentavos(RoundingMode.HALF_UP)
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
    public void criarProduto(Long produtoId, Function<Long, Integer> mapper) {

    }

    @Override
    public CheckoutResponse gerarOrdemDepagamentoCartaoSplit(Long clientId, Long produtoId) {
        // buscar produto cliente externo
        Inscricao inscricao = Inscricao.findById(clientId);
        //gerar funcao para chamada externa
        return null;
    }
}
