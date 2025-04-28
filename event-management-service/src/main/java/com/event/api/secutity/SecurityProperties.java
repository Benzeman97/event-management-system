package com.event.api.secutity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private JwtProperties jwt;
    private OAuthProperties oauth;

    public JwtProperties getJwt() {
        return jwt;
    }

    public void setJwt(JwtProperties jwt) {
        this.jwt = jwt;
    }

    public OAuthProperties getOauth() {
        return oauth;
    }

    public void setOauth(OAuthProperties oauth) {
        this.oauth = oauth;
    }

    public static class JwtProperties {

        private Resource publicKey;

        public Resource getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(Resource publicKey) {
            this.publicKey = publicKey;
        }
    }

    public static class OAuthProperties{

        private String accessTokenUri;
        private String clientId;
        private String clientSecret;
        private String grantType;

        public String getAccessTokenUri() {
            return accessTokenUri;
        }

        public void setAccessTokenUri(String accessTokenUri) {
            this.accessTokenUri = accessTokenUri;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getGrantType() {
            return grantType;
        }

        public void setGrantType(String grantType) {
            this.grantType = grantType;
        }
    }

}
