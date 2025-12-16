import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;
import java.util.UUID;

public class KeyGenerator {
    public static void main(String[] args) throws Exception {
        RSAKey rsaJWK = new RSAKeyGenerator(2048)
                .keyID("test-id")
                .generate();

        System.out.println("----- ВСТАВТЕ ЦЕ В WIREMOCK (jsonBody) -----");
        System.out.println("{ \"keys\": [");
        System.out.println(rsaJWK.toPublicJWK().toJSONString());
        System.out.println("]}");
        System.out.println("---------------------------------------------");
        System.out.println();

        JWSSigner signer = new RSASSASigner(rsaJWK);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("student-user")
                .issuer("http://localhost:8081")
                .expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000))
                .claim("scope", "read write")
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
                claimsSet);

        signedJWT.sign(signer);

        System.out.println("----- ВАШ ТОКЕН (Bearer) -----");
        System.out.println(signedJWT.serialize());
        System.out.println("------------------------------");
    }
}