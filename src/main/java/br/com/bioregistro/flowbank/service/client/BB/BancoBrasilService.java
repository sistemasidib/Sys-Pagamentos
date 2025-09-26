package br.com.bioregistro.flowbank.service.client.BB;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.BoletoBancario;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.ConcursoBancoLogin;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Edital;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import br.com.bioregistro.flowbank.form.Boleto.BB.BoletoBB;
import br.com.bioregistro.flowbank.form.Boleto.BB.BoletoBancoBrasilResponse;
import br.com.bioregistro.flowbank.form.Boleto.BB.BoletosResponse;
import br.com.bioregistro.flowbank.model.TypeOperation;

import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBank;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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



    @Override
    public void baixaBoleto( HttpServerRequest serverRequest,  Long idEdital,ConcursoBancoLogin concursoBancoLogin) {



        String username = this.username;
        String password = this.password;

        String token = getTokenAuth(serverRequest);

        List<BoletosResponse> listaBoletosNaoBaixados = listaBoletosNaoBaixados(token, LocalDate.now(),LocalDate.now(), 'B', 0, 0, username, serverRequest);


        if (listaBoletosNaoBaixados != null){

            //o certo era find num boleto do sys e comparar com nosso numero mas a payment ainda n ta fazendo isso então eu sofro
            //foda sera se eu trago todos os boletos que tem nesse edital que tem a insDtPagamentoNUll é meu melhor pensamento é esse?????????
            //aqui tinha que usar cache kkkkk vou ficar trazendo tds fds.

            List<BoletoBancario> boletosSys = BoletoBancario.buscarPorEdital(idEdital);


            for (BoletoBancario boletoBancario : boletosSys) {

                for(int i = 0; i < listaBoletosNaoBaixados.size(); i++){

                    for(int x = 0; x < listaBoletosNaoBaixados.get(i).getBoletos().size(); x++){

                    if (listaBoletosNaoBaixados.get(i).getBoletos().get(x).getNumeroBoletoBB().equals(boletoBancario.bbaNossoNumero.toString())){

                        Inscricao ins = Inscricao.findById(boletoBancario.insId);
                        if (ins != null) {
                            ins.insDtPagamento = LocalDateTime.now();
                        }

                    }
                    }
                }
            }
        }
    }

    @Override
    public ConcursoBancoLogin getCredencials(Integer bancoId) {
        return null;
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
                                                   int agencia,
                                                   int conta,
                                                   String devAppKey,
                                                   HttpServerRequest serverRequest){
        //faz uma requisição onde informa data, index(tem que mudar de acordo com o retorno da api) -> a cada requisição o index muda no max em 300, então tem que ficar requisitando de acordo com o campo da resposta
        List<BoletosResponse> listaRetornoBoletosResponse = new ArrayList<>();
        int indexCounter = 0;
        int proximoIndice = -1;

        while (indexCounter != proximoIndice){
            //faz a requisição da api
            try {


                 token = token;
                 devAppKey = devAppKey;

                String url = urlListarBoletos
                        + "?indicadorSituacao=" + indicadorSituacao
                        + "&dataInicioMovimento=" + dataInicio.toString()
                        + "&dataFimMovimento=" + dataFim.toString()
                        + "&indice=0"
                        + "&agenciaBeneficiario=" + agencia
                        + "&contaBeneficiario=" + conta;

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + token)  // OAuth2 correto
                        .header("gw-dev-app-key", devAppKey)          // header exigido
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response =
                        client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    // Converte JSON → BoletosResponse
                    BoletosResponse boletosResponse = BoletosResponse.parse(response.body());
                    indexCounter = boletosResponse.getProximoIndice();
                    listaRetornoBoletosResponse.add(boletosResponse);

                    boletosResponse.getBoletos().forEach(b ->
                            System.out.println("Boleto: " + b.getNumeroBoletoBB() +
                                    " - Valor Pago: " + b.getValorPago()
                            + " - Prox Indice: " + boletosResponse.getProximoIndice()));
                } else {
                    System.out.println("Erro " + response.statusCode() + ": " + response.body());
                }

            } catch (Exception e) {
                e.printStackTrace();
                serverRequest.response().setStatusCode(500).end("Falha ao autenticar");
            }



        }



        return listaRetornoBoletosResponse;
    }
    public void liquidarBoleto(){}

}









