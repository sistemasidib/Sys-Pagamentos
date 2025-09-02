package br.com.bioregistro.flowbank.service.client.bradesco;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import br.com.bioregistro.flowbank.form.Boleto.Bradesco.BoletoBradescoRequest;
import br.com.bioregistro.flowbank.form.Boleto.Bradesco.BoletoBradescoResponse;
import br.com.bioregistro.flowbank.model.enuns.TypeOperation;
import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBank;
import io.vertx.core.http.HttpServerRequest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

import static br.com.bioregistro.flowbank.Base64Util.generate;

@ApplicationScoped
public class BradescoService implements ClientBank<BoletoBradescoResponse> {

    @RestClient
    private BradescoClient client;

    @ConfigProperty(name = "app.external.merchant.id")
    String merchant;

    @ConfigProperty(name = "quarkus.rest-client.bradesco-api.url")
    String url;

    public BradescoService(@RestClient BradescoClient client) {
        this.client = client;
    }

    public BoletoBradescoResponse criar(BoletoBradescoRequest request) {
        return client.criarBoleto(request);
    }

    @Override
    public BoletoBradescoResponse processOperationPIX(Inscricao inscricao, TypeOperation operation, HttpServerRequest serverRequest) throws URISyntaxException {

        return gerarLancamentoPix(inscricao, serverRequest);

    }

    @Override
    public BoletoBradescoResponse processOperationBoleto(Long inscricao, TypeOperation operation) {
        return null;
    }

    public BoletoBradescoResponse gerarLancamentoPix(Inscricao inscricao, HttpServerRequest serverRequest) throws URISyntaxException {

        BoletoBradescoRequest request = BoletoBradescoRequest
                .builder()
                .merchantId(merchant)
                .meioPagamento("1200")
                .pedido(BoletoBradescoRequest.Pedido
                        .builder()
                        .numero(generate())
                        .valor(inscricao.localidade.cargo.getVlInscricaoCentavos(RoundingMode.HALF_UP))
                        .descricao("Pagamento via Pix" + " Idecan")
                        .expiracao(300)
                        .formato(1)
                        .build())
                .comprador(BoletoBradescoRequest.Comprador
                        .builder()
                        .nome(inscricao.candidato.canNome)
                        .documento(inscricao.candidato.canCPF)
                        .endereco(BoletoBradescoRequest.Endereco
                                .builder()
                                .cep(inscricao.candidato.canCEP)
                                .logradouro(inscricao.candidato.canEndereco)
                                .numero(inscricao.candidato.canNumero)
                                .complemento(inscricao.candidato.canComplemento)
                                .bairro(inscricao.candidato.canBairro)
                                .cidade(inscricao.candidato.canCidade)
                                .uf(inscricao.candidato.canUFEmissor)
                                .build())
                        .ip(serverRequest.remoteAddress().host())
                        .userAgent(serverRequest.getHeader("User-Agent"))
                        .build())
                .build();

        BradescoClient client2 = RestClientBuilder.newBuilder().baseUri(new URI(url)).followRedirects(true).build(BradescoClient.class);

        return client2.criarPix(request);
    }


}

