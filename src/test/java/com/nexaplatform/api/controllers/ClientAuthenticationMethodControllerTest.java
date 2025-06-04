package com.nexaplatform.api.controllers;

import com.nexaplatform.api.controllers.services.dto.out.AuthenticationMethodDtoOut;
import com.nexaplatform.infrastructura.db.postgres.entities.AuthenticationMethodEntity;
import com.nexaplatform.infrastructura.db.postgres.repositories.AuthenticationMethodRepositoryAdapter;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static com.nexaplatform.providers.authentication.AuthenticationMethodProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientAuthenticationMethodControllerTest extends BaseIntegration {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AuthenticationMethodRepositoryAdapter repositoryAdapter;

    @MockitoBean
    private JWKSource<SecurityContext> jwkSource;

    @BeforeEach
    void setupJwkSourceMock() throws KeySourceException {
        // Cuando cualquier método get(JWKSelector, SecurityContext) sea llamado en el mock jwkSource,
        // retorna el TEST_JWK_SET de tus constantes de test.
        when(jwkSource.get(any(JWKSelector.class), any(SecurityContext.class)))
                .thenReturn(TestConstants.TEST_JWK_SET.getKeys()); // Retorna la lista de JWKs
    }

    // Método para generar un token JWT de prueba
    public String generateTestJwtToken(String username, String role, String userStatus) {
        List<String> rolesList = List.of(role.toUpperCase());

        Instant now = Instant.now();
        return Jwts.builder()
                .header()
                // **CORRECCIÓN AQUÍ: Accede a la clave desde TEST_JWK_SET directamente**
                .add("kid", ((RSAKey) TestConstants.TEST_JWK_SET.getKeys().get(0)).getKeyID())
                .add("alg", SignatureAlgorithm.RS256.name())
                .and()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .issuer(TestConstants.TEST_ISSUER_URI)
                .claim("token_type", "access token")
                .claim("roles", rolesList)
                .claim("status", userStatus)
                .signWith(TestConstants.TEST_RSA_KEY_PAIR.getPrivate())
                .compact();
    }


    @Test
    void create() throws KeySourceException {

        String token = this.generateTestJwtToken("user", "ADMIN", "ACTIVE");

        AuthenticationMethodDtoOut response = webTestClient
                .post()
                .uri("/v1/authenticationmethod")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(getAuthenticationMethodDtoInOne())
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AuthenticationMethodDtoOut.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(response);
        assertThat(getAuthenticationMethodDtoOutOne())
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void getPaginated() {

        List<AuthenticationMethodEntity> authenticationMethodEntities = List.of(
                getAuthenticationMethodEntityOne(),
                getAuthenticationMethodEntityTwo()
        );
        repositoryAdapter.saveAll(authenticationMethodEntities);

        List<AuthenticationMethodDtoOut> response = webTestClient
                .get()
                .uri("/v1/authenticationmethod?page=0&size=10&sort=ASC")
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBodyList(AuthenticationMethodDtoOut.class)
                .returnResult()
                .getResponseBody();

        List<AuthenticationMethodDtoOut> authenticationMethodDtoOuts = List.of(
                getAuthenticationMethodDtoOutOne(),
                getAuthenticationMethodDtoOutTwo()
        );

        assertNotNull(response);
        assertThat(authenticationMethodDtoOuts)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void getById() {

        repositoryAdapter.save(getAuthenticationMethodEntityOne());

        AuthenticationMethodDtoOut response = webTestClient
                .get()
                .uri("/v1/authenticationmethod/1")
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody(AuthenticationMethodDtoOut.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(response);
        assertThat(getAuthenticationMethodDtoOutOne())
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void update() {

        repositoryAdapter.save(getAuthenticationMethodEntityOne());

        AuthenticationMethodDtoOut response = webTestClient
                .put()
                .uri("/v1/authenticationmethod/1")
                .bodyValue(getAuthenticationMethodDtoInTwo())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody(AuthenticationMethodDtoOut.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(response);
        assertThat(getAuthenticationMethodDtoOutTwo().withId(1L))
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void delete() {

        repositoryAdapter.save(getAuthenticationMethodEntityOne());

        webTestClient
                .delete()
                .uri("/v1/authenticationmethod/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}