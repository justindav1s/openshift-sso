package org.jnd.keycloak.web.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.RSATokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Value( "${access.token}" )
	String accesstoken;
	@Value( "${public.key}" )
	String publickey_string;
	@Value( "${realm.url}" )
	String realmUrl;

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
}
