import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAKey;

public class TokenTester {

    String privatekeypath = "/Users/jusdavis/github/openshift-sso/tokenutils/src/main/java/mykey_priv.pem";
    String publickeypath = "/Users/jusdavis/github/openshift-sso/tokenutils/src/main/java/mykey_pub.pem";

    public static void main(String[] args)  {
        TokenTester tt = new TokenTester();
        try {
            tt.shouldCreateAnEmptyRSA256SignedToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shouldCreateAnEmptyRSA256SignedToken() throws Exception {
        RSAKey rsaKey = (RSAKey) PemUtils.readPrivateKeyFromFile(privatekeypath, "RSA");

        String signed = JWT.create().sign(Algorithm.RSA256(rsaKey));

        if (signed == null) {
            throw new RuntimeException("JWT was null");
        }

        String[] parts = signed.split("\\.");
        String headerJson = new String(Base64.decodeBase64(parts[0]), StandardCharsets.UTF_8);
        System.out.println(headerJson);
//        assertThat(headerJson, JsonMatcher.hasEntry("alg", "RS256"));
//        assertThat(headerJson, JsonMatcher.hasEntry("typ", "JWT"));
//        assertThat(parts[1], is("e30"));

        JWTVerifier verified = JWT.require(Algorithm.RSA256((RSAKey) PemUtils.readPublicKeyFromFile(publickeypath, "RSA")))
                .build();

        if (verified == null) {
            throw new RuntimeException("verified was null");
        }
    }


}
