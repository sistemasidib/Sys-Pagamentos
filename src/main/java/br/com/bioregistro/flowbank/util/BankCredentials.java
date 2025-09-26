package br.com.bioregistro.flowbank.util;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.ConcursoBancoLogin;

public class BankCredentials {


    private  ConcursoBancoLogin concursoBancoLogin;


    public BankCredentials(ConcursoBancoLogin concursoBancoLogin) {
        this.concursoBancoLogin = concursoBancoLogin;
    }


    public BankCredentials(Integer bancoId) {
        ConcursoBancoLogin c = ConcursoBancoLogin
                .find("bancoId", bancoId)
                .firstResult();
        if (c == null) {
            throw new IllegalArgumentException("Nenhuma credencial encontrada para bancoId=" + bancoId);
        }
        this.concursoBancoLogin = c;
    }


    public ConcursoBancoLogin getCredenciais() {
        return concursoBancoLogin;
    }
}
