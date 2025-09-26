package br.com.bioregistro.flowbank.exception;

import br.com.bioregistro.flowbank.exception.model.MsgRetorno;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ClientWebFluxExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(MsgRetorno.builder()
                        .success(false)
                        .msg(e.getMessage())
                        .build()).build();
    }

}
