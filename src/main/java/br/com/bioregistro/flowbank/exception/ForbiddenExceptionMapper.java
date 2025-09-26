package br.com.bioregistro.flowbank.exception;

import br.com.bioregistro.flowbank.exception.model.MsgRetorno;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {

    @Override
    public Response toResponse(ForbiddenException e) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(MsgRetorno.builder()
                        .success(false)
                        .msg(e.getMessage())
                        .build()).build();
    }
}

