import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;

import java.io.IOException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.Set;

public class TokenValidator {

    public static void main(String[] args)  {

        String token = args[0];
        System.out.println("TOKEN : "+token);
        try {
            RSAKey key = null;
            try {
                key = (RSAKey) PemUtils.readPublicKeyFromFile("/Users/jusdavis/github/openshift-sso/tokenutils/src/main/java/pub_key.pem", "RSA");
            } catch (IOException e) {
                e.printStackTrace();
            }
            DecodedJWT jwt = JWT.require(Algorithm.RSA256(key))
                    .build()
                    .verify(token);
        }catch (JWTDecodeException exception){
            exception.printStackTrace();
        }

    }
}
