package br.com.bioregistro.flowbank.service.client.providers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@ApplicationScoped
public class AuthRequestHeaderFactory implements ClientHeadersFactory {

    @ConfigProperty(name = "app.external.merchant.id")
    String merchanteId;

    @ConfigProperty(name = "app.external.secutiry.key")
    String securityKey;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders,
                                                 MultivaluedMap<String, String> clientOutgoingHeaders) {
        String token = merchanteId + ":" + securityKey;
        String base64 = Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));

        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        result.add("content-type", "application/json;");
        result.add("accept","*/*");
        result.add("Authorization", "Basic " + base64);
        return result;
    }


}