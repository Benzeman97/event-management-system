package com.event.api.secutity;

import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class OAuth2AuthenticationProvider {

    private final SecurityProperties securityProperties;
    private final RestTemplate restTemplate;

    public OAuth2AuthenticationProvider(SecurityProperties securityProperties, RestTemplate restTemplate){
        this.securityProperties=securityProperties;
        this.restTemplate=restTemplate;
    }

    public OAuth2AccessToken obtainOAuth2AccessToken(String userName, String password){

        SecurityProperties.OAuthProperties oAuthProperties = securityProperties.getOauth();

        String tokenUri = oAuthProperties.getAccessTokenUri();
        String clientId = oAuthProperties.getClientId();
        String clientSecret = oAuthProperties.getClientSecret();
        String grantType = oAuthProperties.getGrantType();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> formData = new HashMap<>();
        formData.put("grant_type", grantType);
        formData.put("username", userName);
        formData.put("password", password);
        formData.put("client_id", clientId);
        formData.put("client_secret", clientSecret);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(formData, headers);

        try {
            ResponseEntity<OAuth2AccessToken> response = restTemplate.exchange(
                    tokenUri, HttpMethod.POST, request, OAuth2AccessToken.class);
            return response.getBody();
        } catch (Exception ex) {
            throw new BadCredentialsException("Authentication failed", ex);
        }
    }
}
