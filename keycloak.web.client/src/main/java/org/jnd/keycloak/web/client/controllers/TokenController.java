package org.jnd.keycloak.web.client.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jnd.keycloak.web.client.model.TPPClient;
import org.jnd.keycloak.web.client.model.TokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import org.keycloak.representations.account.ClientRepresentation;

// http://127.0.0.1:8080/auth/realms/kbc/.well-known/openid-configuration

@RestController
public class TokenController {

    private Log log = LogFactory.getLog(TokenController.class);


    @Value( "${keycloak.url}" )
    String baseUrl;

    @Value( "${aisp.keycloak.realm}" )
    String aispRealm;
    @Value( "${aisp.client.id}" )
    String aispClientId;
    @Value( "${aisp.client.secret}" )
    String aispClientSecret;

    @Value( "${pisp.keycloak.realm}" )
    String pispRealm;
    @Value( "${pisp.client.id}" )
    String pispClientId;
    @Value( "${pisp.client.secret}" )
    String pispClientSecret;


    @RequestMapping(value = "/aisptoken", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getAISPToken() {


        CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory
                = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        log.info("new token");

        String uri = baseUrl+"/auth/realms/"+aispRealm+"/protocol/openid-connect/token";
        log.info("Token URL : "+uri);

        String post_body = "grant_type=client_credentials&client_id="+aispClientId+"&client_secret="+aispClientSecret;
        log.info("Post body : "+post_body);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");


        HttpEntity<String> request = new HttpEntity<>(post_body, headers);

        ResponseEntity<String> exchange =
                restTemplate.exchange(
                        uri,
                        HttpMethod.POST,
                        request,
                        String.class);

        String response = exchange.getBody();
        log.info("Token : "+response);

        ObjectMapper mapper = new ObjectMapper();
        TokenResponse token = null;
        try {
            token = mapper.readValue(response, TokenResponse.class);
            log.info("Access Token : "+token.getAccess_token());
            log.info("Decoded Access Token : "+token.getAccess_tokenDecoded());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(token.getAccess_token(), null, HttpStatus.OK);

    }

    @RequestMapping(value = "/pisptoken", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getPISPToken() {


        CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory
                = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        log.info("new token");

        String uri = baseUrl+"/auth/realms/"+pispRealm+"/protocol/openid-connect/token";
        log.info("Token URL : "+uri);

        String post_body = "grant_type=client_credentials&client_id="+pispClientId+"&client_secret="+pispClientSecret;
        log.info("Post body : "+post_body);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");


        HttpEntity<String> request = new HttpEntity<>(post_body, headers);

        ResponseEntity<String> exchange =
                restTemplate.exchange(
                        uri,
                        HttpMethod.POST,
                        request,
                        String.class);

        String response = exchange.getBody();
        log.info("Token : "+response);

        ObjectMapper mapper = new ObjectMapper();
        TokenResponse token = null;
        try {
            token = mapper.readValue(response, TokenResponse.class);
            log.info("Access Token : "+token.getAccess_token());
            log.info("Decoded Access Token : "+token.getAccess_tokenDecoded());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(token.getAccess_token(), null, HttpStatus.OK);

    }

//    @RequestMapping(value = "/validatetoken", method = RequestMethod.POST, produces = "application/txt")
//    public ResponseEntity validateToken(@RequestBody String token) {
//
//        CloseableHttpClient httpClient
//                = HttpClients.custom()
//                .setSSLHostnameVerifier(new NoopHostnameVerifier())
//                .build();
//        HttpComponentsClientHttpRequestFactory requestFactory
//                = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setHttpClient(httpClient);
//
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
//
//        log.info("validate token");
//
//        String uri = baseUrl+"/auth/realms/"+realm+"/protocol/openid-connect/token/introspect";
//        log.info("Token URL : "+uri);
//
//        String post_body = "client_id="+clientId+"&client_secret="+clientSecret+"&token="+token;
//        log.info("Post body : "+post_body);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/x-www-form-urlencoded");
//
//
//        HttpEntity<String> request = new HttpEntity<>(post_body, headers);
//
//        ResponseEntity<String> exchange =
//                restTemplate.exchange(
//                        uri,
//                        HttpMethod.POST,
//                        request,
//                        String.class);
//
//        String response = exchange.getBody();
//        log.info("Response : "+response);
//
//        return new ResponseEntity(response, null, HttpStatus.OK);
//
//    }
}
