package br.com.bioregistro.flowbank.form.Boleto.BB;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {
    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("token_type")
    public String tokenType; // normalmente "Bearer"

    @JsonProperty("expires_in")
    public long expiresIn;   // em segundos
}
