package com.nexaplatform.application.security;

import com.nimbusds.jose.jwk.source.JWKSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.test.context.ActiveProfiles;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("El bean rsaKeyPair debe estar presente y ser una instancia de KeyPair")
    void rsaKeyPairBeanShouldBePresent() {
        KeyPair keyPair = applicationContext.getBean(KeyPair.class);
        assertNotNull(keyPair);
        assertThat(keyPair.getPublic()).isInstanceOf(RSAPublicKey.class);
    }

    @Test
    @DisplayName("El bean registeredClientRepository debe estar presente y contener el cliente configurado")
    void registeredClientRepositoryBeanShouldBePresentAndContainClient() {
        RegisteredClientRepository repository = applicationContext.getBean(RegisteredClientRepository.class);
        assertNotNull(repository);
        RegisteredClient client = repository.findByClientId("client_token");
        assertNotNull(client);
        assertThat(client.getClientId()).isEqualTo("client_token");
        assertTrue(passwordEncoder.matches("secret", client.getClientSecret()));
        assertThat(client.getClientAuthenticationMethods()).contains(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        assertThat(client.getScopes()).contains(OidcScopes.OPENID);
        assertThat(client.getRedirectUris()).contains("https://oauthdebugger.com/debug");
    }

    @Test
    @DisplayName("El bean jwkSource debe estar presente y ser un JWKSource")
    void jwkSourceBeanShouldBePresent() {
        JWKSource jwkSource = applicationContext.getBean(JWKSource.class);
        assertNotNull(jwkSource);
    }

    @Test
    @DisplayName("El bean jwtDecoder debe estar presente")
    void jwtDecoderBeanShouldBePresent() {
        JwtDecoder jwtDecoder = applicationContext.getBean(JwtDecoder.class);
        assertNotNull(jwtDecoder);
    }

    @Test
    @DisplayName("El bean authorizationServerSettings debe estar presente y tener el issuer correcto")
    void authorizationServerSettingsBeanShouldBePresent() {
        AuthorizationServerSettings settings = applicationContext.getBean(AuthorizationServerSettings.class);
        assertNotNull(settings);
        assertThat(settings.getIssuer()).isEqualTo("http://localhost:9001");
    }

    @Test
    @DisplayName("El bean jwtAuthenticationConverter debe estar presente y ser del tipo correcto")
    void jwtAuthenticationConverterBeanShouldBePresent() {
        Converter<Jwt, AbstractAuthenticationToken> converter = applicationContext.getBean("jwtAuthenticationConverter", Converter.class);
        assertNotNull(converter);
    }
}