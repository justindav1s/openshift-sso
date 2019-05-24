import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Decrypt {
    public static void main(String[] args) throws Exception {


        //String content = new String(Files.readAllBytes(Paths.get("/Users/jusdavis/github/openshift-sso/psd2/crypt/key.pkcs8")));
        Key privateKey = PemUtils.readPrivateKeyFromFile("/Users/jusdavis/github/openshift-sso/psd2/crypt/key.pem", "rsa");

//        System.out.println(content);
//
//        Key privateKey = KeyFactory
//                .getInstance("RSA")
//                .generatePrivate(new PKCS8EncodedKeySpec(content.getBytes()));

        Cipher decrypt = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        decrypt.init(Cipher.DECRYPT_MODE, privateKey, new IvParameterSpec(Base64.getDecoder().decode("aCPKa1JQIR6v4_OcUQH5Vg")));

        String decryptedMessage = new String(decrypt.doFinal(Base64.getDecoder().decode("ddddd")), StandardCharsets.UTF_8);
    }
}