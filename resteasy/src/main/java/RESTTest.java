import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

public class RESTTest {

    public static void main(String[] args) {

        IPAddress ipaddress = new IPAddress("justin", "127.0.0.1");
        System.out.println(ipaddress.isGranted());
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(UriBuilder.fromPath("http://127.0.0.1:8090"));

        IPServices proxy = target.proxy(IPServices.class);

        Response response = proxy.validate(ipaddress);

        IPAddress validatedIP = response.readEntity(IPAddress.class);
        System.out.println(validatedIP.isGranted());


        ipaddress = new IPAddress("justin", "127.0.0.1");
        System.out.println(ipaddress.isGranted());
        ResteasyWebTarget target2 = client.target(UriBuilder.fromPath("http://127.0.0.1:8090/ip/validate"));
        Response response2 = target2.request().post(Entity.entity(ipaddress, "application/json"));
        IPAddress validatedIP2 = response2.readEntity(IPAddress.class);
        System.out.println(validatedIP2.isGranted());
    }
}
