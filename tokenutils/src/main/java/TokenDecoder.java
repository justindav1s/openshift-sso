import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Map;
import java.util.Set;

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
                Claim value = (Claim)claim.getValue();
                System.out.println(name + " : "+value.asString());
            }
            System.out.println("***********");
        } catch (JWTDecodeException exception){
            //Invalid token
        }

    }
}
