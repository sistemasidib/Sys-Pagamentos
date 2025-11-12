package br.com.bioregistro.flowbank.form.Boleto.BB;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "Dados Complementares (Banco do Brasil)")
public class DadosComplementaresBancoBrasil {

    @JsonProperty("vencimento")
    private String vencimento;

    @JsonProperty("cedente")
    private String cedente;

    @JsonProperty("sacado")
    private String sacado;

    @JsonProperty("codigoCedente")
    private String codigoCedente;

    @JsonProperty("numeroDocumento")
    private String numeroDocumento;

    @JsonProperty("valorDocumento")
    private double valorDocumento;

    @JsonProperty("nossoNumero")
    private String nossoNumero;

    @JsonProperty("autenticacaoMecanica")
    private int autenticacaoMecanica;

    @JsonProperty("reciboSacado")
    private int reciboSacado;

    @JsonProperty("banco")
    private int banco;

    @JsonProperty("localPagamento")
    private String localPagamento;

    @JsonProperty("dataDocumento")
    private String dataDocumento;

    @JsonProperty("especieDocumento")
    private String especieDocumento;

    @JsonProperty("aceite")
    private String aceite;

    @JsonProperty("dataProcessamento")
    private String dataProcessamento;

    @JsonProperty("carteira")
    private int carteira;

    @JsonProperty("especie")
    private String especie;

    @JsonProperty("instrucoes")
    private String instrucoes;

    @JsonProperty("informacoesSacado")
    private String informacoesSacado;

    @JsonProperty("cpfCnpj")
    private String cpfCnpj;
}

