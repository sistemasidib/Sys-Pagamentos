package br.com.bioregistro.flowbank.form.Boleto.BB;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrCodeBancoBrasil {
    @Schema(description = "URL do payload do QR Code Pix", nullable = false)
    @JsonProperty("url")
    private String url;

    @Schema(description = "Código que identifica a transação Pix (transactionID)", nullable = false)
    @JsonProperty("txId")
    private String txId;

    @Schema(description = "BR Code no padrão EMV (payload do QR Code Pix)", nullable = false)
    @JsonProperty("emv")
    private String emv;
}
