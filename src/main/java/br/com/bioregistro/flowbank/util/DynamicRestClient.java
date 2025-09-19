package br.com.bioregistro.flowbank.util;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

import java.net.URI;

public class DynamicRestClient {

    public static <T> T buildClient(Class<T> clazz, String url) {
        return RestClientBuilder.newBuilder()
                .baseUri(URI.create(url))
                .build(clazz);
    }

}
