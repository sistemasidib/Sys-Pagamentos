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
    @Transactional
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
        try {
            // Pega o UUID do evento que foi salvo no request
            String eventId = (String) requestContext.getProperty("apiEventId");
            if (eventId != null) {
                ApiEvent event = ApiEvent.findById(Long.valueOf(eventId));
                if (event != null) {
                    log.infof("Updated ApiEvent ID %s with response status %d", eventId, responseContext.getStatus());
                }
            }
        } catch (Exception e) {
            log.error("Erro ao atualizar ApiEvent com status da resposta", e);
        }
    }
}
