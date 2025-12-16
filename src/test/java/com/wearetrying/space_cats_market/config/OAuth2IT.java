package com.wearetrying.space_cats_market.config;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.client.WireMock;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class OAuth2IT {

    @Autowired
    private MockMvc mockMvc;


    @Container
    static GenericContainer<?> wireMockContainer = new GenericContainer<>(DockerImageName.parse("wiremock/wiremock:latest"))
            .withExposedPorts(8080);


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        String wireMockUrl = "http://" + wireMockContainer.getHost() + ":" + wireMockContainer.getFirstMappedPort();

        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
                () -> wireMockUrl + "/.well-known/jwks.json");

        WireMock.configureFor(wireMockContainer.getHost(), wireMockContainer.getFirstMappedPort());
    }

    @Test
    void shouldAllowAccess_WhenTokenIsSignedWithValidPrivateKey() throws Exception {
        RSAKey rsaJWK = new RSAKeyGenerator(2048)
                .keyID("test-id")
                .generate();

        String publicKeysJson = "{ \"keys\": [ " + rsaJWK.toPublicJWK().toJSONString() + " ] }";

        stubFor(WireMock.get(urlEqualTo("/.well-known/jwks.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(publicKeysJson)));

        String token = generateToken(rsaJWK);

        mockMvc.perform(get("/products")
                        .header("Authorization", "Bearer " + token)
                        .header("X-COSMO-KEY", "meow-secret-key-123"))
                .andExpect(status().isOk());
    }

    private String generateToken(RSAKey rsaJWK) throws Exception {
        JWSSigner signer = new RSASSASigner(rsaJWK);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("test-user")
                .issuer("http://localhost:8080")
                .expirationTime(new Date(new Date().getTime() + 60 * 1000))
                .claim("scope", "read write")
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
                claimsSet);

        signedJWT.sign(signer);
        return signedJWT.serialize();
    }
}