package br.com.bioregistro.flowbank.model;

import br.com.bioregistro.flowbank.form.Boleto.BB.BoletoBancoBrasilResponse;
import br.com.bioregistro.flowbank.service.client.strategy.ClientBankOperation;

public record BoletoBBForm() implements ClientBankOperation {


    @Override
    public BoletoBancoBrasilResponse baixaPagamento() {


        return null;
    }

    @Override
    public void solicitaBoleto() {

    }
}
