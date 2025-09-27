package br.com.bioregistro.flowbank.service.client.providers;

import br.com.bio.registro.core.runtime.entities.bioregistro.payment.ApiEvent;
import io.vertx.core.http.HttpServerRequest;
import jakarta.annotation.Priority;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
@Priority(Priorities.USER)
public class OutgoingResponseLogger implements ClientResponseFilter {

    private static final Logger log = Logger.getLogger(OutgoingResponseLogger.class);

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
        try {
            log.infof("response status %d", responseContext.getStatus());
        } catch (Exception e) {
            log.error("Erro ao atualizar ApiEvent com status da resposta", e);
        }
    }
}
