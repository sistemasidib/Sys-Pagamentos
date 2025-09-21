package br.com.bioregistro.flowbank.model;

import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentCompany;
import br.com.bio.registro.core.runtime.entities.bioregistro.payment.PaymentTransaction;

import java.math.BigDecimal;

public record CardSplitForm(
        PaymentTransaction transaction,
        PaymentCompany company,
        BigDecimal amountm

) {


}
