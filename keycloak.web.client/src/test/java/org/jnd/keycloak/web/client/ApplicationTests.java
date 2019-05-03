package org.jnd.keycloak.web.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jnd.keycloak.web.client.model.TPPClient;
import org.jnd.keycloak.web.client.utils.TokenUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.RSATokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureMockMvc
public class ApplicationTests {

	@Value( "${access.token}" )
	String accesstoken;
	@Value( "${public.key}" )
	String publickey_string;
	@Value( "${realm.url}" )
	String realmUrl;

	@Autowired
	private MockMvc mvc;

	private Log log = LogFactory.getLog(ApplicationTests.class);

	private RestTemplate restTemplate = new RestTemplate();

	@Test
	public void verifyToken()  {
		PublicKey publicKey = toPublicKey(publickey_string);
		try {
			RSATokenVerifier rsTV = RSATokenVerifier.create(accesstoken);
			System.out.println("JWS Algorithm : "+rsTV.getHeader().getAlgorithm());
			System.out.println("JWS Type : "+rsTV.getHeader().getType());
			System.out.println("JWS Key Id : "+rsTV.getHeader().getKeyId());

			AccessToken parsedToken = RSATokenVerifier.verifyToken(accesstoken, publicKey, realmUrl,false, true);
			System.out.println("Issued for : "+parsedToken.issuedFor);
			System.out.println("Issuer : "+parsedToken.getIssuer());
			System.out.println("Type : "+parsedToken.getType());
			System.out.println("Roles : "+parsedToken.getRealmAccess().getRoles());
			System.out.println("Username : "+parsedToken.getPreferredUsername());

		} catch (VerificationException e) {
			e.printStackTrace();
		}

	}

	private PublicKey toPublicKey(String publicKeyString) {
		try {
			byte[] publicBytes = Base64.getDecoder().decode(publicKeyString);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			return null;
		}
	}


	public void createClient()  {

		String uri = "http://127.0.0.1:9090/newclient";
		log.info("New Client URL : "+uri);


		TPPClient tppclient = new TPPClient();
		tppclient.setCallback_uri("http://127.0.0.1/my_callback_uri");
		tppclient.setClient_id("test_client1");
		tppclient.setName("TestClient1");
		tppclient.setClient_secret("S@1g0n123");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity(tppclient, headers);

		ResponseEntity<String> exchange =
				this.restTemplate.exchange(
						uri,
						HttpMethod.POST,
						request,
						String.class);

		String responseStr = exchange.getBody();
		log.info("responseStr : "+responseStr);

	}


	public void createClient2()  {

		String uri = "http://127.0.0.1:9090/newclient";
		log.info("New Client URL : "+uri);


		TPPClient tppclient = new TPPClient();
		tppclient.setCallback_uri("http://127.0.0.1/my_callback_uri");
		tppclient.setClient_id("test_client1");
		tppclient.setName("TestClient1");
		tppclient.setClient_secret("S@1g0n123");

		MvcResult result = null;
		String json = null;
		try {
			result = mvc.perform(post("/newclient2")
					.contentType(MediaType.APPLICATION_JSON))

					.andExpect(status().isCreated())
					.andExpect(content()
							.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andReturn();
			json = result.getResponse().getContentAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}


		log.debug("createGadgetProductTest200 result : " + json);

	}
}
