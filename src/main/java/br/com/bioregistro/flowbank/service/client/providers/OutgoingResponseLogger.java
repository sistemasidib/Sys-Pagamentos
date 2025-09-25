package br.com.bioregistro.flowbank.service.client.providers;

import br.com.bio.registro.core.runtime.entities.bioregistro.payment.ApiEvent;
import jakarta.annotation.Priority;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
@Priority(Priorities.USER)
public class OutgoingResponseLogger implements ContainerResponseFilter {

    private static final Logger log = Logger.getLogger(OutgoingResponseLogger.class);

    @Override
    @Transactional
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        try {
            String eventId = (String) requestContext.getProperty("apiEventId");
            if (eventId != null) {
                ApiEvent event = ApiEvent.findById(eventId);
                if (event != null) {
                    event.responseStatus = responseContext.getStatus();
                    event.persist();
                }
            }
        } catch (Exception e) {
            log.error("Erro ao atualizar status da resposta", e);
        }
    }
}
