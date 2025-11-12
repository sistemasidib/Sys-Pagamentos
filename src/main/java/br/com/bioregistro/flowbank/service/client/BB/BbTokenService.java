package br.com.bioregistro.flowbank.service.client.BB;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.ConcursoBancoLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

import java.io.StringReader;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class BbTokenService {





    HttpClient httpClient = HttpClient.newHttpClient();

    public String getTokenBancoBrasil(ConcursoBancoLogin login, String urlAuth) {
        try {
            // DEBUG: Verifique as credenciais
            System.out.println("[BB Token] 🔍 Debug Credenciais:");
            System.out.println("[BB Token] Client ID: " + login.loginId);
            System.out.println("[BB Token] Client Secret: " + (login.senha != null ? "***" + login.senha.substring(Math.max(0, login.senha.length() - 3)) : "null"));
            System.out.println("[BB Token] Dev App Key: " + login.devAppKey);
            System.out.println("[BB Token] URL: " + urlAuth);

            // Verifique se as credenciais estão no formato correto
            if (login.loginId == null || login.senha == null) {
                System.err.println("[BB Token] ❌ Credenciais nulas");
                return null;
            }

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(30))
                    .build();

            String auth = Base64.getEncoder()
                    .encodeToString((login.loginId + ":" + login.senha).getBytes());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlAuth))
                    .header("Authorization", "Basic " + auth)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("[BB Token] 📡 Status: " + response.statusCode());

            if (response.statusCode() == 200) {
                JsonObject json = Json.createReader(new StringReader(response.body())).readObject();
                String token = json.getString("access_token");
                System.out.println("[BB Token] ✅ Token obtido com sucesso");
                return token;
            } else {
                System.err.println("[BB Token] ❌ Erro HTTP " + response.statusCode());
                if (response.body().contains("Cloudflare")) {
                    System.err.println("[BB Token] 🛡️  Bloqueado pelo Cloudflare");
                    System.err.println("[BB Token] 💡 Possíveis causas:");
                    System.err.println("[BB Token]   - IP bloqueado");
                    System.err.println("[BB Token]   - Ambiente errado (produção vs sandbox)");
                    System.err.println("[BB Token]   - Credenciais inválidas");
                    System.err.println("[BB Token]   - Falta de certificado client");
                }
                return null;
            }
        } catch (Exception e) {
            System.err.println("[BB Token] 💥 Exception: " + e.getMessage());
            return null;
        }
    }
    }


