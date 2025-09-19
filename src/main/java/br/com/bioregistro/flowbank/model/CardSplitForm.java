package br.com.bioregistro.flowbank.model;

import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentCompany;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentTransaction;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentTransactionSplit;

import java.math.BigDecimal;

public record CardSplitForm(
        PaymentTransaction transaction,
        PaymentCompany company,
        BigDecimal amountm

) {

    public PaymentTransactionSplit toPaymentTransactionSplit() {
        PaymentTransactionSplit split = new PaymentTransactionSplit();
        split.transaction = transaction;
        split.company = company;
        split.amount = amountm;
        return split;
    }

}
