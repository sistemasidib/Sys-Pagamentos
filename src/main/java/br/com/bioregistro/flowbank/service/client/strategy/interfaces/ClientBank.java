package br.com.bioregistro.flowbank.service.client.strategy.interfaces;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import br.com.bioregistro.flowbank.model.PixForm;
import br.com.bioregistro.flowbank.model.TypeOperation;
import io.vertx.core.http.HttpServerRequest;

import java.net.URISyntaxException;
import java.util.function.Function;

public interface ClientBank<T  extends ClientBankResponse, P , R> {

    T processOperationPIX(Inscricao candidato, TypeOperation operation, HttpServerRequest serverRequest) throws URISyntaxException;

    T processOperationBoleto(Integer inscricao, TypeOperation operation);

    T processOperationPIX(Inscricao candidato, TypeOperation operation, HttpServerRequest serverRequest, Function<P, R> mapper) throws URISyntaxException;

    T processOperationBaixa(String clientCredencial, TypeOperation operation, HttpServerRequest serverRequest, Function<P, R> mapper) throws URISyntaxException;

}
