package br.com.bioregistro.flowbank.form.Boleto.BB;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoletoBB {

    @JsonProperty("numeroBoletoBB")
    private String numeroBoletoBB;

    @JsonProperty("dataRegistro")
    private String dataRegistro;

    @JsonProperty("dataVencimento")
    private String dataVencimento;

    @JsonProperty("valorOriginal")
    private Double valorOriginal;

    @JsonProperty("carteiraConvenio")
    private Integer carteiraConvenio;

    @JsonProperty("variacaoCarteiraConvenio")
    private Integer variacaoCarteiraConvenio;

    @JsonProperty("codigoEstadoTituloCobranca")
    private Integer codigoEstadoTituloCobranca;

    @JsonProperty("estadoTituloCobranca")
    private String estadoTituloCobranca;

    @JsonProperty("contrato")
    private Long contrato;

    @JsonProperty("dataMovimento")
    private String dataMovimento;

    @JsonProperty("dataCredito")
    private String dataCredito;

    @JsonProperty("valorAtual")
    private Double valorAtual;

    @JsonProperty("valorPago")
    private Double valorPago;
}
