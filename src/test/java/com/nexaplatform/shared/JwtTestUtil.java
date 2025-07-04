package com.nexaplatform.shared;

import com.nexaplatform.application.security.SecurityConfig;
import com.nexaplatform.domain.models.Role;
import com.nexaplatform.domain.models.User;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.*;

@Component
@Profile("test")
public class JwtTestUtil {

    private final RSAPrivateKey privateKey;
    private final String keyId;

    public JwtTestUtil(@Autowired KeyPair rsaKeyPair) {
        this.privateKey = (RSAPrivateKey) rsaKeyPair.getPrivate();
        this.keyId = SecurityConfig.RSA_KEY_ID;
    }

    public Jwt createJwt(String subject, List<String> roles, Map<String, Object> additionalClaims) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(3600);

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .issuer("http://localhost:9001")
                .subject(subject)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiry))
                .claim("token_type", "access token")
                .claim("roles", roles);

        if (additionalClaims != null) {
            additionalClaims.forEach(claimsBuilder::claim);
        }

        JWTClaimsSet claimsSet = claimsBuilder.build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(this.keyId)
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        try {
            JWSSigner signer = new RSASSASigner(privateKey);
            signedJWT.sign(signer);
            String tokenValue = signedJWT.serialize();

            return new Jwt(
                    tokenValue,
                    now,
                    expiry,
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().getClaims()
            );
        } catch (Exception e) {
            throw new IllegalStateException("Error al crear JWT para test", e);
        }
    }

    public Jwt createJwt(User user) {

        String subject = user.getEmail();
        Map<String, Object> additionalClaims = new HashMap<>();
        additionalClaims.put("user_id", user.getId()); // Añade el ID del usuario
        additionalClaims.put("first_name", user.getFirstName());
        additionalClaims.put("last_name", user.getLastName());
        additionalClaims.put("status", user.getStatus());
        List<String> roles = user.getRoles().stream()
                .map(Role::getName).toList();

        return createJwt(subject, roles, additionalClaims);
    }

    public String getToken(String subject, List<String> roles) {
        return "Bearer " + createJwt(subject, roles, null).getTokenValue();
    }

    public String getToken(String subject, List<String> roles, Map<String, Object> additionalClaims) {
        return "Bearer " + createJwt(subject, roles, additionalClaims).getTokenValue();
    }

    public String getToken(User user) {
        return "Bearer " + createJwt(user).getTokenValue();
    }
}
