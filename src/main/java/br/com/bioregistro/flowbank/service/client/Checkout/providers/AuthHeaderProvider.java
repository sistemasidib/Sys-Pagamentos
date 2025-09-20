package br.com.bioregistro.flowbank.service.client.Checkout.providers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@ApplicationScoped
public class AuthHeaderProvider implements ClientHeadersFactory {

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders,
                                                 MultivaluedMap<String, String> clientOutgoingHeaders) {
        String base64 = "ce379af0-91e4-4c90-b6d2-68f71039ccb4";

        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        result.add("Content-Type", "application/json");
        result.add("Accept","*/*");
        result.add("Authorization", "Bearer " + base64);
        return result;
    }


}