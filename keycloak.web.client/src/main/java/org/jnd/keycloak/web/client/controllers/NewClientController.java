package org.jnd.keycloak.web.client.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jnd.keycloak.web.client.model.TPPClient;
import org.jnd.keycloak.web.client.utils.TokenUtils;
import org.keycloak.client.registration.Auth;
import org.keycloak.client.registration.ClientRegistration;
import org.keycloak.client.registration.ClientRegistrationException;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class NewClientController {

    private Log log = LogFactory.getLog(NewClientController.class);

    @Value( "${keycloak.url}" )
    String baseUrl;

    private RestTemplate restTemplate = new RestTemplate();
    private TokenUtils tokenUtils = new TokenUtils();

    @RequestMapping(value = "/newclient", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity newclient2(@RequestBody TPPClient tppclient) {

        log.info("NewClientController new client for realm :" + tppclient.getRealm());

        String accessToken = tokenUtils.getAccessToken(tppclient.getRealm());
        log.info("Access Token : "+accessToken);

        ObjectMapper om = new ObjectMapper();
        try {
            String json = om.writeValueAsString(tppclient);
            log.info("tppclient : "+json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ClientRepresentation client = new ClientRepresentation();
        List redirect_uris = new ArrayList();
        redirect_uris.add(tppclient.getCallback_uri());

        client.setRedirectUris(redirect_uris);
        client.setName(tppclient.getName());
        client.setClientId(tppclient.getClient_id());
        client.setDirectAccessGrantsEnabled(true);
        client.setServiceAccountsEnabled(true);
        client.setStandardFlowEnabled(false);
        client.setSecret(tppclient.getClient_secret());

        String uri = baseUrl+"/auth/realms/"+tppclient.getRealm()+"/clients-registrations/default";
        log.info("Client Reg URL : "+uri);


        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        headers.add("Content-Type", "application/json");

        String clientreqjson = null;
        try {
             clientreqjson = om.writeValueAsString(client);
            log.info("clientreqjson : "+clientreqjson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpEntity<String> request = new HttpEntity<>(clientreqjson, headers);

        ResponseEntity<String> exchange =
                restTemplate.exchange(
                        uri,
                        HttpMethod.POST,
                        request,
                        String.class);

        String response = exchange.getBody();
        log.info("Response : "+response);

        String registrationAccessToken = client.getRegistrationAccessToken();
        String secret = client.getSecret();
        log.debug("Client Secret : "+secret);

        tppclient.setClient_secret(secret);

        return new ResponseEntity(accessToken, null, HttpStatus.OK);

    }
}
