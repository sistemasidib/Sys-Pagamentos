package br.com.bioregistro.flowbank.exception;

import br.com.bioregistro.flowbank.exception.model.MsgRetorno;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class APIExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(MsgRetorno.builder()
                        .success(false)
                        .msg(e.getMessage())
                        .build()).build();
    }


}