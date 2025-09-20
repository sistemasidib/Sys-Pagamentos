package br.com.bioregistro.flowbank.service.client.Checkout.model.request;

public record PessoaReq(
        String nome_completo,
        String cpf,
        String telefone,
        String email
) {
}
