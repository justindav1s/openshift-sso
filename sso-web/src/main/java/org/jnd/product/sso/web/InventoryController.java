package org.jnd.product.sso.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jnd.microservices.model.Product;
import org.jnd.microservices.model.utils.B3HeaderHelper;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InventoryController {

    private Log log = LogFactory.getLog(InventoryController.class);

    @Value( "${inventory.host}" )
    String inventory_host;

    private RestTemplate restTemplate = new RestTemplate();;


    @GetMapping("/products")
    public String products(Principal principal, Model model) {
        log.info(" products(Model model) : "+model);
        ResponseEntity<List> exchange = this.getAllProducts(principal);
        model.addAttribute("products", exchange.getBody());

        return "products";
    }

    public ResponseEntity<Product> getProduct(String id, HttpHeaders headers) {

        log.info("ProductRepositoryProxy get Product id : "+id);

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

        KeycloakPrincipal kPrinciple = (KeycloakPrincipal)principal;
        String token = ((KeycloakPrincipal)principal).getKeycloakSecurityContext().getTokenString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+token);

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

    public ResponseEntity<List> getProductsofType(String type, HttpHeaders headers) {

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

    public ResponseEntity<List> getProductTypes(HttpHeaders headers) {


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

}
