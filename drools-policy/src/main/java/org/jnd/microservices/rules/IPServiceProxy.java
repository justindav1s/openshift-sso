package org.jnd.microservices.rules;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class IPServiceProxy {

    private String base_url = null;

    public IPServiceProxy(String base_url)    {
        this.base_url = base_url;
    }

    private RestTemplate restTemplate = new RestTemplate();

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(IPServiceProxy.class);

    public IPAddress validate(IPAddress address) {

        log.info("IPServiceProxy : validate address : " + address.getAddress());

        try {

            HttpEntity<IPAddress> request = new HttpEntity(address);

            ResponseEntity<IPAddress> exchange =
                    this.restTemplate.exchange(
                            base_url + "/ip/validate",
                            HttpMethod.POST,
                            request,
                            IPAddress.class);

            address = exchange.getBody();

        }
        catch (HttpClientErrorException hcee)   {
            //do nothing this user does not exist, the 404 causes the exception to be thrown
            log.info("IPServiceProxy address not valid : " + address.getAddress());
        }

        return address;
    }

}
