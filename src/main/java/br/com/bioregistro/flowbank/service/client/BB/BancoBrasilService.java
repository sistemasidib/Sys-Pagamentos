package br.com.bioregistro.flowbank.service.client.BB;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.BancoEdital;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.BoletoBancario;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.ConcursoBancoLogin;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import br.com.bioregistro.flowbank.form.Boleto.BB.BoletoBancoBrasilResponse;
import br.com.bioregistro.flowbank.form.Boleto.BB.BoletosResponse;
import br.com.bioregistro.flowbank.model.TypeOperation;

import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBank;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@ApplicationScoped
public class BancoBrasilService implements ClientBank<BoletoBancoBrasilResponse, Inscricao, Integer> {
    @ConfigProperty(name = "quarkus.rest-client.bancobrasil-api.urlAuth")
    String urlAuthBB;

    @ConfigProperty(name = "quarkus.rest-client.bancobrasil-api.username")
    String username;

    @ConfigProperty(name = "quarkus.rest-client.bancobrasil-api.password")
    String password;

    @ConfigProperty(name = "quarkus.rest-client.bancobrasil-api.urlListarBoletos")
    String urlListarBoletos;
    @Inject
    BbTokenService bbTokenService;


    private HttpClient getHttpClient() {
        return HttpClient.newHttpClient();
    }

    @Override
    public BoletoBancoBrasilResponse processOperationPIX(Inscricao candidato, TypeOperation operation, HttpServerRequest serverRequest) throws URISyntaxException {
        return null;
    }

    @Override
    public BoletoBancoBrasilResponse processOperationBoleto(Integer inscricao, TypeOperation operation) {

        if (operation == TypeOperation.BOLETO){

        }

        if (operation == TypeOperation.PIX){

        }
        return null;
    }

    @Override
    public BoletoBancoBrasilResponse processOperationPIX(Inscricao candidato, TypeOperation operation, HttpServerRequest serverRequest, Function<Inscricao, Integer> mapper) throws URISyntaxException {
        return null;
    }

    @Override
    public BoletoBancoBrasilResponse processOperationBaixa(String clientCredencial, TypeOperation operation, HttpServerRequest serverRequest, Function<Inscricao, Integer> mapper) throws URISyntaxException {
        return null;
    }


    private String obterTokenViaClientCredentials(ConcursoBancoLogin login) {
       // String token = tokenService.gerarTokenBanco(login.getLoginId(), login.getSenha(), login.getDevAppKey());

        return null;
    }

    @Override
    @Transactional
    public void baixaBoleto(HttpServerRequest serverRequest, Integer idEdital, ConcursoBancoLogin concursoBancoLogin) {



        String username = concursoBancoLogin.loginId;
        String password = concursoBancoLogin.senha;
        String devApikey = concursoBancoLogin.devAppKey;

        String token = "";
        String bearer = "";



        String bearerToken = bbTokenService.getTokenBancoBrasil(concursoBancoLogin, urlAuthBB);

        if (bearerToken == null || bearerToken.isEmpty()) {
            System.err.println("[BB] Falha ao obter token para edital " + idEdital);
            return;
        }


        List<BoletosResponse> listaBoletosNaoBaixados = listaBoletosNaoBaixados(
                bearerToken,
                LocalDate.now(),
                LocalDate.now(),
                'B',
                idEdital,
                devApikey);


        if (listaBoletosNaoBaixados != null && !listaBoletosNaoBaixados.isEmpty()) {
            processarBoletosBaixados(listaBoletosNaoBaixados, idEdital);
        } else {
            System.out.println("[BB] Nenhum boleto encontrado para edital " + idEdital);
        }

//        if (listaBoletosNaoBaixados != null){
//
//
//
//            List<BoletoBancario> boletosSys = BoletoBancario.buscarPorEdital(idEdital);
//
//
//            for (BoletoBancario boletoBancario : boletosSys) {
//
//                for(int i = 0; i < listaBoletosNaoBaixados.size(); i++){
//
//                    for(int x = 0; x < listaBoletosNaoBaixados.get(i).getBoletos().size(); x++){
//
//                    if (listaBoletosNaoBaixados.get(i).getBoletos().get(x).getNumeroBoletoBB().equals(boletoBancario.bbaNumeroDocumento)){
//
//                        Inscricao ins = Inscricao.findById(boletoBancario.inscricao);
//                        if (ins != null) {
//                            ins.insDtPagamento = LocalDateTime.now();
//                        }
//
//                    }
//                    }
//                }
//            }
//        }
    }

    @Override
    public void baixaBoleto(HttpServerRequest serverRequest, Long editalId, ConcursoBancoLogin login) {
        ClientBank.super.baixaBoleto(serverRequest, editalId, login);
    }

    @Override
    public void baixaBoleto(Long editalId, ConcursoBancoLogin login) {

    }


    void processarBoletosBaixados(List<BoletosResponse> boletosResponses, Integer editalId) {
        List<BoletoBancario> boletosSys = BoletoBancario.buscarPorEdital(editalId);
        int boletosAtualizados = 0;

        for (BoletosResponse response : boletosResponses) {
            for (var boletoBB : response.getBoletos()) {
                for (BoletoBancario boletoSys : boletosSys) {
                    if (boletoBB.getNumeroBoletoBB().equals(boletoSys.bbaNumeroDocumento)) {
                        Inscricao ins = Inscricao.findById(boletoSys.inscricao);
                        if (ins != null && ins.insDtPagamento == null) {
                            ins.insDtPagamento = LocalDateTime.now();
                            boletosAtualizados++;
                            System.out.printf("[BB] Boleto %s marcado como pago%n", boletoBB.getNumeroBoletoBB());
                        }
                        break;
                    }
                }
            }
        }

        System.out.printf("[BB] Total de %d boletos atualizados para edital %d%n", boletosAtualizados, editalId);
    }
    public String getTokenAuth(HttpServerRequest serverRequest) {
        String token = "";
        try {
            String auth = Base64.getEncoder()
                    .encodeToString((username + ":" + password).getBytes());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlAuthBB))
                    .header("Authorization", "Basic " + auth)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                token = response.body();
                serverRequest.response().end("Token recebido: " + response.body());
            } else {
                serverRequest.response()
                        .setStatusCode(response.statusCode())
                        .end("Erro ao autenticar: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            serverRequest.response().setStatusCode(500).end("Falha ao autenticar");
        }
        return token;

    }
    public List<BoletosResponse> listaBoletosNaoBaixados(String token,
                                                         LocalDate dataInicio,
                                                         LocalDate dataFim,
                                                         char indicadorSituacao,
                                                         Integer editalId,
                                                         String devAppKey) {
        List<BoletosResponse> listaRetornoBoletosResponse = new ArrayList<>();
        int indexCounter = 0;
        int proximoIndice = -1;

        Optional<BancoEdital> contaBancaria = BancoEdital.findByEditalId(editalId);
        String conta = "";
        String agencia = "";

        if (contaBancaria.isPresent()) {
            conta = contaBancaria.get().bedConta;
            agencia = contaBancaria.get().bedAgencia;
        } else {
            System.err.println("[BB] Conta bancária não encontrada para edital " + editalId);
            return listaRetornoBoletosResponse;
        }

        // Loop de paginação
        while (proximoIndice != indexCounter) {
            try {
                String url = urlListarBoletos
                        + "?indicadorSituacao=" + indicadorSituacao
                        + "&dataInicioMovimento=" + dataInicio.toString()
                        + "&dataFimMovimento=" + dataFim.toString()
                        + "&indice=" + indexCounter  // Usa o índice atual
                        + "&agenciaBeneficiario=" + agencia
                        + "&contaBeneficiario=" + conta;

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + token)
                        .header("gw-dev-app-key", devAppKey)
                        .header("Accept", "application/json")
                        .GET()
                        .build();
                HttpClient client = getHttpClient();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    BoletosResponse boletosResponse = BoletosResponse.parse(response.body());
                    listaRetornoBoletosResponse.add(boletosResponse);

                    // Atualiza índice para próxima página
                    proximoIndice = boletosResponse.getProximoIndice();
                    indexCounter = proximoIndice; // Avança para o próximo índice

                    System.out.printf("[BB] Página %d - %d boletos encontrados%n",
                            indexCounter, boletosResponse.getBoletos().size());

                } else {
                    System.err.println("[BB] Erro HTTP " + response.statusCode() + ": " + response.body());
                    break;
                }

            } catch (Exception e) {
                System.err.println("[BB] Erro ao buscar boletos: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }

        return listaRetornoBoletosResponse;
    }
    public void liquidarBoleto(){}

}









