package org.jnd.keycloak.web.client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.OAuth2Constants;
import org.keycloak.RSATokenVerifier;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.common.VerificationException;
import org.keycloak.jose.jws.JWSHeader;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.KeysMetadataRepresentation.KeyMetadataRepresentation;

import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;
import java.util.Map;

public class KeycloakClientAuthExample {

    public static void main(String[] args) {

        KeycloakClientFacade facade = new KeycloakClientFacade( //
                "https://sso.datr.eu:8443/auth" //
                , "PSD2" //
                , "kbci" //
                , "c22faa6a-778d-4076-96bc-dce205292e63" //
        );

        // Get raw AccessToken string for client credentials grant
        System.out.println(facade.getAccessTokenString());

        // Get decoded AccessToken for client credentials grant
//    System.out.println(facade.getAccessToken());

        // Get decoded AccessToken for password credentials grant
        AccessToken accessToken = facade.getAccessToken("test_user2", "123456");

        System.out.println(accessToken);

        System.out.println(accessToken.getSubject());
    }

    static class KeycloakClientFacade {

        private final String serverUrl;

        private final String realmId;

        private final String clientId;

        private final String clientSecret;

        public KeycloakClientFacade(String serverUrl, String realmId, String clientId, String clientSecret) {
            this.serverUrl = serverUrl;
            this.realmId = realmId;
            this.clientId = clientId;
            this.clientSecret = clientSecret;
        }

        public AccessToken getAccessToken() {
            return getAccessToken(newKeycloakBuilderWithClientCredentials().build());
        }

        public String getAccessTokenString() {
            return getAccessTokenString(newKeycloakBuilderWithClientCredentials().build());
        }

        public AccessToken getAccessToken(String username, String password) {
            System.out.println("*** ENTRY : getAccessToken(String username, String password)");
            AccessToken at = getAccessToken(newKeycloakBuilderWithPasswordCredentials(username, password).build());
            System.out.println("at : "+at);
            System.out.println("*** EXIT : getAccessToken(String username, String password)");
            return at;
        }

        public String getAccessTokenString(String username, String password) {
            return getAccessTokenString(newKeycloakBuilderWithPasswordCredentials(username, password).build());
        }

        private AccessToken getAccessToken(Keycloak keycloak) {
            System.out.println("*** ENTRY : getAccessToken(Keycloak keycloak)");
            AccessToken at = extractAccessTokenFrom(keycloak, getAccessTokenString(keycloak));
            System.out.println("at : "+at);
            System.out.println("*** EXIT : getAccessToken(Keycloak keycloak)");
            return at;
        }

        private String getAccessTokenString(Keycloak keycloak) {
            System.out.println("*** ENTRY : getAccessTokenString(Keycloak keycloak)");
            AccessTokenResponse tokenResponse = getAccessTokenResponse(keycloak);
            System.out.println("*** tokenResponse : "+tokenResponse);
            return tokenResponse == null ? null : tokenResponse.getToken();
        }

        private AccessToken extractAccessTokenFrom(Keycloak keycloak, String token) {
            System.out.println("*** ENTRY : extractAccessTokenFrom(Keycloak keycloak, String token)");
            System.out.println("*** Keycloak : "+keycloak);
            System.out.println("*** token : "+token);

            if (token == null) {
                return null;
            }

            try {
                RSATokenVerifier verifier = RSATokenVerifier.create(token);
                PublicKey publicKey = getRealmPublicKey(keycloak, verifier.getHeader());
                return verifier.realmUrl(getRealmUrl()) //
                        .publicKey(publicKey) //
                        .verify() //
                        .getToken();
            } catch (VerificationException e) {
                return null;
            }

        }

        private KeycloakBuilder newKeycloakBuilderWithPasswordCredentials(String username, String password) {
            return newKeycloakBuilderWithClientCredentials() //
                    .username(username) //
                    .password(password) //
                    .grantType(OAuth2Constants.PASSWORD);
        }

        private KeycloakBuilder newKeycloakBuilderWithClientCredentials() {
            return KeycloakBuilder.builder() //
                    .realm(realmId) //
                    .serverUrl(serverUrl)//
                    .clientId(clientId) //
                    .clientSecret(clientSecret) //
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS);
        }

        private AccessTokenResponse getAccessTokenResponse(Keycloak keycloak) {
            try {
                return keycloak.tokenManager().getAccessToken();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        public String getRealmUrl() {
            return serverUrl + "/realms/" + realmId;
        }

        public String getRealmCertsUrl() {
            return getRealmUrl() + "/protocol/openid-connect/certs";
        }

        private PublicKey getRealmPublicKey(Keycloak keycloak, JWSHeader jwsHeader) {

            // Variant 1: use openid-connect /certs endpoint
            //return retrievePublicKeyFromCertsEndpoint(jwsHeader);

            // Variant 2: use the Public Key referenced by the "kid" in the JWSHeader
            // in order to access realm public key we need at least realm role... e.g. view-realm
            //return retrieveActivePublicKeyFromKeysEndpoint(keycloak, jwsHeader);

            // Variant 3: use the active RSA Public Key exported by the PublicRealmResource representation
            return retrieveActivePublicKeyFromPublicRealmEndpoint();
        }

        private PublicKey retrievePublicKeyFromCertsEndpoint(JWSHeader jwsHeader) {
            try {
                ObjectMapper om = new ObjectMapper();
                @SuppressWarnings("unchecked")
                Map<String, Object> certInfos = om.readValue(new URL(getRealmCertsUrl()).openStream(), Map.class);

                List<Map<String, Object>> keys = (List<Map<String, Object>>) certInfos.get("keys");

                Map<String, Object> keyInfo = null;
                for (Map<String, Object> key : keys) {
                    String kid = (String) key.get("kid");

                    if (jwsHeader.getKeyId().equals(kid)) {
                        keyInfo = key;
                        break;
                    }
                }

                if (keyInfo == null) {
                    return null;
                }

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                String modulusBase64 = (String) keyInfo.get("n");
                String exponentBase64 = (String) keyInfo.get("e");

                // see org.keycloak.jose.jwk.JWKBuilder#rs256
                Decoder urlDecoder = Base64.getUrlDecoder();
                BigInteger modulus = new BigInteger(1, urlDecoder.decode(modulusBase64));
                BigInteger publicExponent = new BigInteger(1, urlDecoder.decode(exponentBase64));

                PublicKey pKey = keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
                RSAPublicKeySpec rsa = new RSAPublicKeySpec(modulus, publicExponent);
                System.out.println("******* Encoded Public Key : "+pKey.getEncoded());
                return pKey;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private PublicKey retrieveActivePublicKeyFromPublicRealmEndpoint() {

            try {
                ObjectMapper om = new ObjectMapper();
                System.out.println("******* RealmEndpoint : "+getRealmUrl());
                @SuppressWarnings("unchecked")
                Map<String, Object> realmInfo = om.readValue(new URL(getRealmUrl()).openStream(), Map.class);
                System.out.println("******* publicKeyString : "+(String) realmInfo.get("public_key"));
                return toPublicKey((String) realmInfo.get("public_key"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private PublicKey retrieveActivePublicKeyFromKeysEndpoint(Keycloak keycloak, JWSHeader jwsHeader) {

            List<KeyMetadataRepresentation> keys = keycloak.realm(realmId).keys().getKeyMetadata().getKeys();

            String publicKeyString = null;
            for (KeyMetadataRepresentation key : keys) {
                if (key.getKid().equals(jwsHeader.getKeyId())) {
                    System.out.println("******* publicKeyString : "+publicKeyString);
                    publicKeyString = key.getPublicKey();
                    break;
                }
            }

            return toPublicKey(publicKeyString);
        }

        public PublicKey toPublicKey(String publicKeyString) {
            try {
                System.out.println("******* publicKeyString : "+publicKeyString);
                byte[] publicBytes = Base64.getDecoder().decode(publicKeyString);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return keyFactory.generatePublic(keySpec);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                return null;
            }
        }
    }
}