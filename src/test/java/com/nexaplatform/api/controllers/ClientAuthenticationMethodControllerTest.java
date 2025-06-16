package com.nexaplatform.api.controllers;

import com.nexaplatform.api.controllers.services.dto.out.AuthenticationMethodDtoOut;
import com.nexaplatform.infrastructura.db.postgres.entities.AuthenticationMethodEntity;
import com.nexaplatform.infrastructura.db.postgres.repositories.AuthenticationMethodRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.nexaplatform.providers.authentication.AuthenticationMethodProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientAuthenticationMethodControllerTest extends BaseIntegration {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String POST_AUTHENTICATIONMETHOD = "/v1/authenticationmethod";
    public static final String UPDATE_AUTHENTICATIONMETHOD = "/v1/authenticationmethod/1";
    public static final String DELETE_AUTHENTICATIONMETHOD = "/v1/authenticationmethod/1";
    public static final String GET_AUTHENTICATIONMETHOD_BY_ID = "/v1/authenticationmethod/1";
    public static final String AUTHENTICATIONMETHOD_PAGE_0_SIZE_10_SORT_ASC = "/v1/authenticationmethod?page=0&size=10&sort=ASC";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AuthenticationMethodRepositoryAdapter repositoryAdapter;

    @Test
    void create() {
        AuthenticationMethodDtoOut response = webTestClient
                .post()
                .uri(POST_AUTHENTICATIONMETHOD)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
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
                .uri(AUTHENTICATIONMETHOD_PAGE_0_SIZE_10_SORT_ASC)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
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
                .uri(GET_AUTHENTICATIONMETHOD_BY_ID)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
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
                .uri(UPDATE_AUTHENTICATIONMETHOD)
                .bodyValue(getAuthenticationMethodDtoInTwo())
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
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
                .uri(DELETE_AUTHENTICATIONMETHOD)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .exchange()
                .expectStatus().isNoContent();
    }
}