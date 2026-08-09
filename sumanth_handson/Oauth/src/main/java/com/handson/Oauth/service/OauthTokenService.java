package com.handson.Oauth.service;

import com.handson.Oauth.entity.TokenRequest;
import com.handson.Oauth.entity.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OauthTokenService {

    private final RestClient restClient = RestClient.create();
    private final String tokenUri;
    private final String clientId;
    private final String clientSecret;
    private final String audience;

    public OauthTokenService(
            @Value("${auth0.token-uri}") String tokenUri,
            @Value("${auth0.client-id}") String clientId,
            @Value("${auth0.client-secret}") String clientSecret,
            @Value("${auth0.audience}") String audience
    ) {
        this.tokenUri = tokenUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.audience = audience;
    }
    ///this is sumanth

    public String getAccessToken() {
        if (clientId.isBlank() || clientSecret.isBlank()) {
            throw new IllegalStateException("Set AUTH0_CLIENT_ID and AUTH0_CLIENT_SECRET before requesting an Auth0 token.");
        }

        TokenRequest request = new TokenRequest();
        request.setClientId(clientId);
        request.setClientSecret(clientSecret);
        request.setAudience(audience);
        request.setGrantType("client_credentials");

        TokenResponse response = restClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(TokenResponse.class);

        return response.getAccess_token();
    }
}
