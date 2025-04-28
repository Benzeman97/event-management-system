package com.event.api.secutity;

import com.event.api.util.WebClientUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class OAuth2AuthenticationProvider {

    private SecurityProperties securityProperties;
    private final WebClientUtil webClientUtil;

    public OAuth2AuthenticationProvider(SecurityProperties securityProperties, WebClientUtil webClientUtil){
        this.securityProperties=securityProperties;
        this.webClientUtil=webClientUtil;
    }

    public OAuth2AccessToken obtainOAuth2AccessToken(String userName, String password){

        SecurityProperties.OAuthProperties oAuthProperties = securityProperties.getOauth();

        String tokenUri = oAuthProperties.getAccessTokenUri();
        String clientId = oAuthProperties.getClientId();
        String clientSecret = oAuthProperties.getClientSecret();

        String body = "grant_type=password&username=" + userName +
                "&password=" + password +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret;

        Mono<OAuth2AccessToken> accessTokenMono = webClientUtil.createWebClient(tokenUri)
                .post()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(OAuth2AccessToken.class);

        try {
            return accessTokenMono.block();
        } catch (Exception ex) {
            throw new BadCredentialsException("Authentication failed", ex);
        }

    }
}
