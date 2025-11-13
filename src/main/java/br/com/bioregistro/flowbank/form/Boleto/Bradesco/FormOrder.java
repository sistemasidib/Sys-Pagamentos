package br.com.bioregistro.flowbank.form.Boleto.Bradesco;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import java.math.BigDecimal;

public record FormOrder(
        String cargoDesc,
        BigDecimal carVlInscricao,
        String editalId,
        String editalNome,
        String localidadeId,
        String canNome,
        String canCpf,
        String canTelefone,
        String canEmail
) {
    public static FormOrder from(String clientId, String alias) {

        if ("IDC".equals(alias)) {
            Inscricao inscricao = Inscricao.findById(clientId);

            return new FormOrder(
                    inscricao.localidade.cargo.carDescricao,
                    inscricao.localidade.cargo.carVlInscricao,
                    inscricao.localidade.cargo.edital.ediId.toString(),
                    inscricao.localidade.cargo.edital.ediEntidade,
                    inscricao.localidade.locId.toString(),
                    inscricao.candidato.canNome,
                    inscricao.candidato.canCPF,
                    inscricao.candidato.canTelefone1,
                    inscricao.candidato.canEmail
            );

        } else {
            br.com.bio.registro.core.runtime.entities.idib.dbo.Inscricao inscricao =
                    br.com.bio.registro.core.runtime.entities.idib.dbo.Inscricao.findById(clientId);

            return new FormOrder(
                    inscricao.localidade.cargo.carDescricao,
                    inscricao.localidade.cargo.carVlInscricao,
                    inscricao.localidade.cargo.edital.ediId.toString(),
                    inscricao.localidade.cargo.edital.ediEntidade,
                    inscricao.localidade.locId.toString(),
                    inscricao.candidato.canNome,
                    inscricao.candidato.canCPF,
                    inscricao.candidato.canTelefone1,
                    inscricao.candidato.canEmail
            );
        }
    }
}
