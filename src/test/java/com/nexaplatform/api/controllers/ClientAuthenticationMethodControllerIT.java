package com.nexaplatform.api.controllers;

import com.nexaplatform.api.services.dto.out.AuthenticationMethodDtoOut;
import com.nexaplatform.infrastructura.db.postgres.entities.AuthenticationMethodEntity;
import com.nexaplatform.infrastructura.db.postgres.repositories.AuthenticationMethodRepositoryAdapter;
import com.nexaplatform.shared.BaseIntegration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.nexaplatform.providers.authentication.AuthenticationMethodProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClientAuthenticationMethodControllerIT extends BaseIntegration {

    public static final String ID = "/1";
    public static final String PATH_AUTHENTICATIONMETHODS = "/v1/authenticationmethods";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AuthenticationMethodRepositoryAdapter repositoryAdapter;

    @Test
    void create() {
        AuthenticationMethodDtoOut response = webTestClient
                .post()
                .uri(PATH_AUTHENTICATIONMETHODS)
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

//    @Test
//    void create_403() {
//        ErrorResponse response = webTestClient
//                .post()
//                .uri(PATH_AUTHENTICATIONMETHODS)
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, getToken(List.of()))
//                .bodyValue(getAuthenticationMethodDtoInOne())
//                .exchange()
//                .expectStatus().isForbidden()
//                .expectBody(ErrorResponse.class)
//                .returnResult()
//                .getResponseBody();
//
//        assertEquals(ERROR_CODE_ACCESS_DENIED, response.getCode());
//        assertEquals(NO_TIENES_LOS_PERMISOS_NECESARIOS, response.getMessage());
//    }

    @Test
    void getPaginated() {

        List<AuthenticationMethodEntity> authenticationMethodEntities = List.of(
                getAuthenticationMethodEntityOne(),
                getAuthenticationMethodEntityTwo()
        );
        repositoryAdapter.saveAll(authenticationMethodEntities);

        List<AuthenticationMethodDtoOut> response = webTestClient
                .get()
                .uri(PATH_AUTHENTICATIONMETHODS + "?page=0&size=10&sort=ASC")
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
                .uri(PATH_AUTHENTICATIONMETHODS + ID)
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
                .uri(PATH_AUTHENTICATIONMETHODS + ID)
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
                .uri(PATH_AUTHENTICATIONMETHODS + ID)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .exchange()
                .expectStatus().isNoContent();
    }
}