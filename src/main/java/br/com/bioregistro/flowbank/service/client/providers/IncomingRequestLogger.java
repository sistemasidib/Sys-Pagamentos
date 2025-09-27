package br.com.bioregistro.flowbank.service.client.providers;

import br.com.bio.registro.core.runtime.entities.bioregistro.payment.ApiEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.http.HttpServerRequest;
import jakarta.annotation.Priority;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;

@Provider
@Priority(Priorities.USER)
public class IncomingRequestLogger implements ContainerRequestFilter {

    private static final Logger log = Logger.getLogger(IncomingRequestLogger.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    @Transactional
    public void filter(ContainerRequestContext requestContext) {
        try {
            // Lê o corpo
            InputStream originalStream = requestContext.getEntityStream();
            String body = "";
            if (originalStream != null) {
                body = new BufferedReader(new InputStreamReader(originalStream))
                        .lines()
                        .collect(Collectors.joining("\n"));
                InputStream newStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
                requestContext.setEntityStream(newStream);
            }

            log.infof("Incoming request: %s %s", requestContext.getMethod(), requestContext.getUriInfo().getPath()); log.info("Headers:"); requestContext.getHeaders().forEach((k, v) -> log.infof("- %s: %s", k, v)); log.infof("Body: %s", body);

        } catch (Exception e) {
            log.error("Erro ao registrar requisição", e);
        }
    }
}
