package br.com.bioregistro.flowbank.model;

import br.com.bioregistro.flowbank.model.TypeClient;
import br.com.bioregistro.flowbank.model.TypeOperation;

public record ClientResquestPIX(
        TypeOperation operation,
        TypeClient client,
        Long inscricao
) {
}
