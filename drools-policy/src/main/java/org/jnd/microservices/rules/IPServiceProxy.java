package org.jnd.microservices.rules;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

public class IPServiceProxy {

    private String base_url = null;

    public IPServiceProxy(String base_url)    {
        this.base_url = base_url;
    }

    public IPAddress validate(IPAddress address) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(UriBuilder.fromPath(this.base_url+"/ip/validate"));
        Response response = target.request().post(Entity.entity(address, MediaType.APPLICATION_JSON_TYPE));
        IPAddress validatedIP = response.readEntity(IPAddress.class);
        return validatedIP;

    }
}
