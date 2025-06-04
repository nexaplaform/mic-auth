package com.nexaplatform.api.controllers;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

public class TestConstants {

    public static final String TEST_ISSUER_URI = "http://localhost:9001"; // Debe coincidir con tu AuthorizationServerSettings

    public static final KeyPair TEST_RSA_KEY_PAIR;
    public static final JWKSet TEST_JWK_SET; // <-- Añade esto para acceder al JWKSet directamente
    public static final JWKSource<SecurityContext> TEST_JWK_SOURCE;

    static {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            TEST_RSA_KEY_PAIR = keyPairGenerator.generateKeyPair();

            RSAPublicKey publicKey = (RSAPublicKey) TEST_RSA_KEY_PAIR.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) TEST_RSA_KEY_PAIR.getPrivate();
            RSAKey rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString()) // Genera un ID de clave
                    .build();
            TEST_JWK_SET = new JWKSet(rsaKey); // <-- Asigna el JWKSet aquí
            TEST_JWK_SOURCE = new ImmutableJWKSet<>(TEST_JWK_SET); // Usa el JWKSet ya creado

        } catch (Exception ex) {
            throw new IllegalStateException("Failed to generate test RSA KeyPair", ex);
        }
    }
}
