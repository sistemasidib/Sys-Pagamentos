package br.com.bioregistro.flowbank.form.Boleto.BB;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiarioBancoBrasil {

    @Schema(description = "Agência do beneficiário", nullable = false)
    @JsonProperty("agencia")
    private int agencia;

    @Schema(description = "Conta corrente do beneficiário", nullable = false)
    @JsonProperty("contaCorrente")
    private int contaCorrente;

    @Schema(description = "Código do tipo de endereço do beneficiário", nullable = false)
    @JsonProperty("tipoEndereco")
    private int tipoEndereco;

    @Schema(description = "Nome do logradouro do beneficiário", nullable = false)
    @JsonProperty("logradouro")
    private String logradouro;

    @Schema(description = "Bairro do beneficiário", nullable = false)
    @JsonProperty("bairro")
    private String bairro;

    @Schema(description = "Cidade do beneficiário", nullable = false)
    @JsonProperty("cidade")
    private String cidade;

    @Schema(description = "Identificador da cidade do beneficiário", nullable = false)
    @JsonProperty("codigoCidade")
    private int codigoCidade;

    @Schema(description = "Sigla do Estado do beneficiário", nullable = false)
    @JsonProperty("uf")
    private String uf;

    @Schema(description = "Código Postal do beneficiário", nullable = false)
    @JsonProperty("cep")
    private int cep;

    @Schema(description = "Indicador de prova de vida do beneficiário", nullable = false)
    @JsonProperty("indicadorComprovacao")
    private String indicadorComprovacao;
}
