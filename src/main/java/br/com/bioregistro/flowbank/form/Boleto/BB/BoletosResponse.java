package br.com.bioregistro.flowbank.form.Boleto.BB;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoletosResponse {

    @JsonProperty("indicadorContinuidade")
    private String indicadorContinuidade;

    @JsonProperty("boletos")
    private List<BoletoBB> boletos;

    @JsonProperty("quantidadeRegistros")
    private Integer quantidadeRegistros;

    @JsonProperty("proximoIndice")
    private Integer proximoIndice;

    public static BoletosResponse parse(String json) {
        JsonObject root = new JsonObject(json);

        BoletosResponse resp = new BoletosResponse();
        resp.setIndicadorContinuidade(root.copy().getString("indicadorContinuidade"));
        resp.setQuantidadeRegistros(root.copy().getInteger("quantidadeRegistros"));
        resp.setProximoIndice(root.copy().getInteger("proximoIndice"));

        List<BoletoBB> boletos = new ArrayList<>();
        JsonArray arr = root.copy().getJsonArray("boletos");
        if (arr != null) {
            for (int i = 0; i < arr.toBuffer().length(); i++) {
                JsonObject obj = arr.getJsonObject(i);
                BoletoBB b = BoletoBB.builder()
                        .numeroBoletoBB(obj.copy().getString("numeroBoletoBB"))
                        .dataRegistro(obj.copy().getString("dataRegistro"))
                        .dataVencimento(obj.copy().getString("dataVencimento"))
                        .valorOriginal(obj.copy().getDouble("valorOriginal"))
                        .carteiraConvenio(obj.copy().getInteger("carteiraConvenio"))
                        .variacaoCarteiraConvenio(obj.copy().getInteger("variacaoCarteiraConvenio"))
                        .codigoEstadoTituloCobranca(obj.copy().getInteger("codigoEstadoTituloCobranca"))
                        .estadoTituloCobranca(obj.copy().getString("estadoTituloCobranca"))
                        .contrato(obj.copy().getLong("contrato"))
                        .dataMovimento(obj.copy().getString("dataMovimento"))
                        .dataCredito(obj.copy().getString("dataCredito"))
                        .valorAtual(obj.copy().getDouble("valorAtual"))
                        .valorPago(obj.copy().getDouble("valorPago"))
                        .build();
                boletos.add(b);
            }
        }
        resp.setBoletos(boletos);

        return resp;
    }
}

