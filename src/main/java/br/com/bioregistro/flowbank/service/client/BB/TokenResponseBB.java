package br.com.bioregistro.flowbank.service.client.BB;

public final class TokenResponseBB {
    public String access_token;
    public String token_type; // "Bearer"
    public long   expires_in; // em segundos
}
