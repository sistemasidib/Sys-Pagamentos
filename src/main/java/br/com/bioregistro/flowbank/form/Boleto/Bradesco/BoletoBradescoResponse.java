package br.com.bioregistro.flowbank.form.Boleto.Bradesco;



import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBankResponse;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@RegisterForReflection
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoletoBradescoResponse implements ClientBankResponse {

    private String merchantId;
    private String meioPagamento;
    private Pedido pedido;
    private Pix pix;
    private Status status;

    @RegisterForReflection
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Pedido {
        private String numero;
        private Integer valor;
        private String descricao;
    }

    @RegisterForReflection
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Pix {
        private String dataAtualizacao; // pode virar LocalDateTime se preferir
        private String token;
        private String urlAcesso;
    }

    @RegisterForReflection
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Status {
        private Integer codigo;
        private String mensagem;
    }
}
