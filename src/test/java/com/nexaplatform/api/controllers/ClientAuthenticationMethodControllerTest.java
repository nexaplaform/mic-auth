package com.nexaplatform.api.controllers;

import com.nexaplatform.api.controllers.services.dto.out.AuthenticationMethodDtoOut;
import com.nexaplatform.infrastructura.db.postgres.entities.AuthenticationMethodEntity;
import com.nexaplatform.infrastructura.db.postgres.repositories.AuthenticationMethodRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.nexaplatform.providers.authentication.AuthenticationMethodProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientAuthenticationMethodControllerTest extends BaseIntegration {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AuthenticationMethodRepositoryAdapter repositoryAdapter;

    @Test
    void create() {

        AuthenticationMethodDtoOut response = webTestClient
                .post()
                .uri("/v1/authenticationmethod")
                .contentType(MediaType.APPLICATION_JSON)
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