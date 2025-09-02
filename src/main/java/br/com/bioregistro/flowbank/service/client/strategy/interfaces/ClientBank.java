package br.com.bioregistro.flowbank.service.client.strategy.interfaces;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import br.com.bioregistro.flowbank.model.PixForm;
import br.com.bioregistro.flowbank.model.enuns.TypeOperation;
import io.vertx.core.http.HttpServerRequest;

import java.net.URISyntaxException;
import java.util.function.Function;

public interface ClientBank<T  extends ClientBankResponse> {

    T processOperationPIX(Inscricao candidato, TypeOperation operation, HttpServerRequest serverRequest) throws URISyntaxException;

    T processOperationBoleto(Long inscricao, TypeOperation operation);

    T processOperationPIX(Inscricao candidato, TypeOperation operation, HttpServerRequest serverRequest, Function<PixForm, Long> mapper) throws URISyntaxException;

}
