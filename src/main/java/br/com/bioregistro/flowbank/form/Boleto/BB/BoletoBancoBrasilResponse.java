package br.com.bioregistro.flowbank.form.Boleto.BB;

import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBankResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoletoBancoBrasilResponse implements ClientBankResponse {

    @Schema(description = "Dados do Beneficiário", nullable = false)
    @JsonProperty("beneficiario")
    private BeneficiarioBancoBrasil beneficiario;

    @Schema(description = "Dados do QRCode", nullable = false)
    @JsonProperty("qrCode")
    private QrCodeBancoBrasil qrCode;

    @Schema(description = "Identificador exclusivo do boleto", nullable = false)
    @JsonProperty("numero")
    private String numero;

    @Schema(description = "Número da carteira do convênio de cobrança", nullable = false)
    @JsonProperty("numeroCarteira")
    private int numeroCarteira;

    @Schema(description = "Número da variação da carteira do convênio de cobrança", nullable = false)
    @JsonProperty("numeroVariacaoCarteira")
    private int numeroVariacaoCarteira;

    @Schema(description = "Identificação do cliente", nullable = false)
    @JsonProperty("codigoCliente")
    private int codigoCliente;

    @Schema(description = "Linha digitável do boleto", nullable = false)
    @JsonProperty("linhaDigitavel")
    private String linhaDigitavel;

    @Schema(description = "Código de barras numérico do boleto", nullable = false)
    @JsonProperty("codigoBarraNumerico")
    private String codigoBarraNumerico;

    @Schema(description = "Número do contrato de cobrança do boleto", nullable = false)
    @JsonProperty("numeroContratoCobranca")
    private int numeroContratoCobranca;

    @Schema(description = "Dados complementares do boleto")
    @JsonProperty("dadosComplementares")
    private DadosComplementaresBancoBrasil dadosComplementares;
}
