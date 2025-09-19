package br.com.bioregistro.flowbank.model;

import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentProvider;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentTransaction;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.enuns.PaymentTransactionType;

import java.math.BigDecimal;

public record PaymentForm(
    PaymentProvider provider,
    String externalId,
    BigDecimal amount,
    String currency,
    PaymentTransactionType method,
    String status

) {

    public PaymentTransaction toPaymentTransaction() {
        PaymentTransaction paymentTransaction = new PaymentTransaction();

        paymentTransaction.amount = amount;
        paymentTransaction.currency = currency;
        paymentTransaction.status = status;
        paymentTransaction.externalId = externalId;
        paymentTransaction.paymentMethod = method;
        paymentTransaction.provider = provider;

        return paymentTransaction;
    }

}
