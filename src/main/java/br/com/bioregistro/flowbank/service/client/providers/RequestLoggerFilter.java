package br.com.bioregistro.flowbank.service.client.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class RequestLoggerFilter implements ClientRequestFilter {

    private static final Logger log = Logger.getLogger(RequestLoggerFilter.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void filter(ClientRequestContext requestContext) {
        log.infof("Sending request to: %s", requestContext.getUri());

        log.info("Headers:");
        requestContext.getHeaders().forEach((key, values) -> {
            log.infof("- %s: %s", key, values);
        });

        Object entity = requestContext.getEntity();
        if (entity != null) {
            try {
                String json = mapper.writeValueAsString(entity);
                log.infof("Body: %s", json);
            } catch (Exception e) {
                log.error("Erro ao serializar o body para JSON", e);
            }
        } else {
            log.info("Body: null");
        }
    }
}
