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
    public FormOrder(String clientId, String alias) {
        // Variáveis temporárias
        String cargoDesc;
        BigDecimal carVlInscricao;
        String editalId;
        String editalNome;
        String localidadeId;
        String canNome;
        String canCpf;
        String canTelefone;
        String canEmail;

        if (alias.equals("IDC")) {
            Inscricao inscricao = Inscricao.findById(clientId);

            cargoDesc = inscricao.localidade.cargo.carDescricao;
            carVlInscricao = inscricao.localidade.cargo.carVlInscricao;
            editalId = inscricao.localidade.cargo.edital.ediId.toString();
            editalNome = inscricao.localidade.cargo.edital.ediEntidade;
            localidadeId = inscricao.localidade.locId.toString();
            canNome = inscricao.candidato.canNome;
            canCpf = inscricao.candidato.canCPF;
            canTelefone = inscricao.candidato.canTelefone1;
            canEmail = inscricao.candidato.canEmail;

        } else {
            br.com.bio.registro.core.runtime.entities.idib.dbo.Inscricao inscricao =
                    br.com.bio.registro.core.runtime.entities.idib.dbo.Inscricao.findById(clientId);

            cargoDesc = inscricao.localidade.cargo.carDescricao;
            carVlInscricao = inscricao.localidade.cargo.carVlInscricao;
            editalId = inscricao.localidade.cargo.edital.ediId.toString();
            editalNome = inscricao.localidade.cargo.edital.ediEntidade;
            localidadeId = inscricao.localidade.locId.toString();
            canNome = inscricao.candidato.canNome;
            canCpf = inscricao.candidato.canCPF;
            canTelefone = inscricao.candidato.canTelefone1;
            canEmail = inscricao.candidato.canEmail;
        }

        // ✅ Única chamada ao construtor canônico
        this(cargoDesc, carVlInscricao, editalId, editalNome, localidadeId, canNome, canCpf, canTelefone, canEmail);
    }
}
