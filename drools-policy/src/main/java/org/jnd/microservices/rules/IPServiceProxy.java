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
            log.info("IPServiceProxy : 1");
            HttpEntity<IPAddress> request = new HttpEntity(address);
            log.info("IPServiceProxy : 2");
            ResponseEntity<IPAddress> exchange =
                    this.restTemplate.exchange(
                            base_url + "/ip/validate",
                            HttpMethod.POST,
                            request,
                            IPAddress.class);
            log.info("IPServiceProxy : 3");
            address = exchange.getBody();
            log.info("IPServiceProxy : 4");
            address.setGranted(true);
        }
        catch (HttpClientErrorException hcee)   {
            log.info("IPServiceProxy : 5");
            //do nothing this user does not exist, the 404 causes the exception to be thrown
            log.info("IPServiceProxy address not valid : " + address.getAddress());
        }
        log.info("IPServiceProxy : 6");
        return address;
    }

}
