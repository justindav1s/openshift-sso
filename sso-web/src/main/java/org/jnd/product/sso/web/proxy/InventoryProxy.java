package org.jnd.product.sso.web.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jnd.microservices.model.Product;
import org.jnd.product.sso.web.utils.LoggingRequestInterceptor;
import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryProxy {

    private Log log = LogFactory.getLog(InventoryProxy.class);

    @Value( "${inventory.host}" )
    String inventory_host;

    private RestTemplate restTemplate = new RestTemplate();;

    public ResponseEntity<Product> getProduct(String id, Principal principal) {

        log.info("ProductRepositoryProxy get Product id : "+id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+getAccessToken(principal));

        ResponseEntity<Product> exchange =
                this.restTemplate.exchange(
                        "http://"+inventory_host+"/products/{id}",
                        HttpMethod.GET,
                        new HttpEntity<byte[]>(headers),
                        new ParameterizedTypeReference<Product>() {},
                        id);

        Product resp = exchange.getBody();
        log.info("Product Response : "+resp);

        if (exchange == null)
            throw new RuntimeException();

        return exchange;
    }

    public ResponseEntity<List> getAllProducts(Principal principal) {

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new LoggingRequestInterceptor());
        //restTemplate.setInterceptors(interceptors);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+getAccessToken(principal));

        log.info(" getAllProducts Enter");

        ResponseEntity<List> exchange =
                this.restTemplate.exchange(
                        "http://"+inventory_host+"/products/all",
                        HttpMethod.GET,
                        new HttpEntity<byte[]>(headers),
                        new ParameterizedTypeReference<List>() {});

        log.info("All Products Response : "+exchange.getBody());


        if (exchange == null)
            throw new RuntimeException();

        log.info(" getAllProducts Leave");

        return exchange;
    }

    public ResponseEntity<List> getProductsofType(Principal principal, String type) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+getAccessToken(principal));

        ResponseEntity<List> exchange =
                this.restTemplate.exchange(
                        "http://"+inventory_host+"/products/type/{type}",
                        HttpMethod.GET,
                        new HttpEntity<byte[]>(headers),
                        new ParameterizedTypeReference<List>() {},
                        type);

        log.debug("AFTER B3Headers");
        log.debug("Product Response : "+exchange.getBody());

        if (exchange == null)
            throw new RuntimeException();

        return exchange;
    }

    public ResponseEntity<List> getProductTypes(Principal principal) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+getAccessToken(principal));

        ResponseEntity<List> exchange =
                this.restTemplate.exchange(
                        "http://"+inventory_host+"/products/types",
                        HttpMethod.GET,
                        new HttpEntity<byte[]>(headers),
                        new ParameterizedTypeReference<List>() {});

        log.debug("Product types response : "+exchange.getBody());

        if (exchange == null)
            throw new RuntimeException();

        return exchange;
    }


    public String getAccessToken(Principal principal) {
        KeycloakPrincipal kPrinciple = (KeycloakPrincipal)principal;
        String accesstoken = ((KeycloakPrincipal)principal).getKeycloakSecurityContext().getTokenString();
        log.info("Access Token : "+accesstoken);
        return accesstoken;
    }
}
