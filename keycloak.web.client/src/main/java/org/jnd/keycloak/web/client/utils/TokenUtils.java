package org.jnd.keycloak.web.client.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jnd.keycloak.web.client.controllers.TokenController;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class TokenUtils {

    private Log log = LogFactory.getLog(TokenUtils.class);

    private RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken(String realm) {

        String uri = "http://127.0.0.1:9090/"+realm+"token";
        log.info("Token URL : "+uri);

        ResponseEntity<String> exchange =
                this.restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        null,
                        String.class);

        String token = exchange.getBody();
        log.info("Access Token : "+token);
        return token;

    }
}
