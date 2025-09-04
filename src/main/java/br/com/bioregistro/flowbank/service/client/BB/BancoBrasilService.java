package br.com.bioregistro.flowbank.service.client.BB;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.Inscricao;
import br.com.bioregistro.flowbank.form.Boleto.BB.BoletoBancoBrasilResponse;
import br.com.bioregistro.flowbank.model.TypeOperation;

import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBank;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.URISyntaxException;
import java.util.function.Function;

@ApplicationScoped
public class BancoBrasilService implements ClientBank<BoletoBancoBrasilResponse, Inscricao, Integer> {


    @Override
    public BoletoBancoBrasilResponse processOperationPIX(Inscricao candidato, TypeOperation operation, HttpServerRequest serverRequest) throws URISyntaxException {
        return null;
    }

    @Override
    public BoletoBancoBrasilResponse processOperationBoleto(Integer inscricao, TypeOperation operation) {
        return null;
    }

    @Override
    public BoletoBancoBrasilResponse processOperationPIX(Inscricao candidato, TypeOperation operation, HttpServerRequest serverRequest, Function<Inscricao, Integer> mapper) throws URISyntaxException {
        return null;
    }

    @Override
    public BoletoBancoBrasilResponse processOperationBaixa(String clientCredencial, TypeOperation operation, HttpServerRequest serverRequest, Function<Inscricao, Integer> mapper) throws URISyntaxException {
        return null;
    }
}
