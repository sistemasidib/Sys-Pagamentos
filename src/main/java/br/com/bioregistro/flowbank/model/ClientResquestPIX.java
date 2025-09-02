package br.com.bioregistro.flowbank.model;

import br.com.bioregistro.flowbank.model.enuns.TypeClient;
import br.com.bioregistro.flowbank.model.enuns.TypeOperation;

public record ClientResquestPIX(
        TypeOperation operation,
        TypeClient client,
        Long inscricao
) {
}
