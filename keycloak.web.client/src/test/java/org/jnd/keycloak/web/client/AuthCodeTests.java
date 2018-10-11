package org.jnd.keycloak.web.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jnd.keycloak.web.client.controllers.TokenController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthCodeTests {

	@Value( "${keycloak.realm}" )
	String realm;
	@Value( "${keycloak.url}" )
	String baseUrl;

	String clientId = "tpp1";
	String redirecturl= "http://127.0.0.1:9090/getcode";
	String user = "test_user2";
    String password = "123456";


	private Log log = LogFactory.getLog(TokenController.class);

	@Test
	public void getAuthCode()  {
		CloseableHttpClient httpClient
				= HttpClients.custom()
                .disableRedirectHandling()
				.setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory
				= new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);


		RestTemplate restTemplate = new RestTemplate(requestFactory);

		log.info("Get Auth code");

		String query_string = "scope=openid&response_type=code&client_id="+clientId+"&redirect_uri="+redirecturl;
		log.info("query_string : "+query_string);

		String uri = baseUrl+"/auth/realms/"+realm+"/protocol/openid-connect/auth";
		log.info("Auth Code URL : "+uri);

		uri = uri+"?"+query_string;

		log.info("Auth Code URL : "+uri);


		HttpHeaders headers = new HttpHeaders();

		String creds = user+":"+password;
		String base64Creds = new String(Base64.getEncoder().encode(creds.getBytes()));
        log.info("base64Creds : "+base64Creds);
        headers.add("Authorization", "Basic "+base64Creds);


		HttpEntity<String> request = new HttpEntity<>(headers);

		ResponseEntity<String> exchange =
				restTemplate.exchange(
						uri,
						HttpMethod.GET,
						request,
						String.class);

		HttpHeaders responseHeaders = exchange.getHeaders();
		log.info("Location : "+responseHeaders.get("Location"));

		String response = exchange.getBody();
		log.info("Response : "+response);


	}

}
