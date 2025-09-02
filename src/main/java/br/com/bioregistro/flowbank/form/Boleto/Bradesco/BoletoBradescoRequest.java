package br.com.bioregistro.flowbank.form.Boleto.Bradesco;


import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@RegisterForReflection
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoletoBradescoRequest {

    private String merchantId;                       // "merchant_id"
    private String meioPagamento;                    // "meio_pagamento"
    private Pedido pedido;
    private Comprador comprador;
    private String tokenRequestConfirmacaoPagamento; // "token_request_confirmacao_pagamento"

    @RegisterForReflection
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Pedido {
        private String numero;
        private Long valor; // precisão de moeda
        private String descricao;
        private Integer expiracao;
        private Integer formato;
    }

    @RegisterForReflection
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Comprador {
        private String nome;
        private String documento;
        private Endereco endereco;
        private String ip;
        private String userAgent; // vira "user_agent"
    }

    @RegisterForReflection
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Endereco {
        private String cep;
        private String logradouro;
        private String numero;
        private String complemento;
        private String bairro;
        private String cidade;
        private String uf;
    }
}
