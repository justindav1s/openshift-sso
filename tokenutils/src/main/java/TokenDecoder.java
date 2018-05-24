import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class TokenDecoder {

    public static void main(String[] args)  {

        String token = args[0];
        //System.out.println(token);
        try {
            DecodedJWT jwt = JWT.decode(token);
            System.out.println("***********");
            System.out.println("Audience : "+jwt.getAudience());
            System.out.println("Subject : "+jwt.getSubject());
            System.out.println("Type : "+jwt.getType());
            Set claims = jwt.getClaims().entrySet();
            for (Map.Entry claim : jwt.getClaims().entrySet())  {
                String name = (String)claim.getKey();
                Claim value = null;
                if ("allowed-origins".equals(name)) {
                    Claim c = (Claim)claim.getValue();
                    String[] vals = c.asArray(String.class);
                    for (String str : vals) {
                        System.out.println(name + " : " + str);
                    }
                }
                else if ("nbf".equals(name) || "iat".equals(name) || "exp".equals(name) || "auth_time".equals(name)) {
                    Claim c = (Claim)claim.getValue();
                    Long val = c.asLong();
                    System.out.println(name + " : " + val);
                }
                else if ("resource_access".equals(name)) {
                    Claim c = (Claim)claim.getValue();
                    Map val = c.asMap();
                    System.out.println(name + " : " + val);
                }
                else {
                    value = (Claim) claim.getValue();
                    System.out.println(name + " : "+value.asString());
                }

            }
            System.out.println("***********");
        } catch (JWTDecodeException exception){
            exception.printStackTrace();
        }

    }
}
