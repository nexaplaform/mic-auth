package com.nexaplatform.api.controllers;

import com.nexaplatform.api.controllers.services.dto.out.ErrorResponse;
import com.nexaplatform.api.controllers.services.dto.out.RoleDtoOut;
import com.nexaplatform.infrastructura.db.postgres.repositories.RoleRepositoryAdapter;
import com.nexaplatform.shared.BaseIntegration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.nexaplatform.domain.errors.Error.ROLE_NOT_FOUND;
import static com.nexaplatform.providers.authentication.AuthenticationMethodProvider.ROLE_ADMIN;
import static com.nexaplatform.providers.user.RoleProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RoleControllerIT extends BaseIntegration {

    public static final String ID = "/1";
    public static final String PATH_ROLES = "/v1/roles";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RoleRepositoryAdapter roleRepository;

    @Test
    void create() {

        RoleDtoOut response = webTestClient
                .post()
                .uri(PATH_ROLES)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .bodyValue(getRoleDtoInOne())
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(RoleDtoOut.class)
                .returnResult()
                .getResponseBody();

        assertThat(getRoleDtoInOne())
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void getPaginated() {

        roleRepository.saveAll(List.of(getRoleEntityOne(), getRoleEntityTwo()));

        List<RoleDtoOut> response = webTestClient
                .get()
                .uri(PATH_ROLES + "?page=0&size=10&sort=ASC")
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(RoleDtoOut.class)
                .returnResult()
                .getResponseBody();

        assertThat(List.of(getRoleDtoOutOne(), getRoleDtoOutTwo()))
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void getById() {

        roleRepository.save(getRoleEntityOne());

        RoleDtoOut response = webTestClient
                .get()
                .uri(PATH_ROLES + ID)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(RoleDtoOut.class)
                .returnResult()
                .getResponseBody();

        assertThat(getRoleDtoOutOne())
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void getById_Not_Found() {

        ErrorResponse response = webTestClient
                .get()
                .uri(PATH_ROLES + ID)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(response);
        assertEquals(ROLE_NOT_FOUND.getCode(), response.getCode());
        assertEquals(String.format(ROLE_NOT_FOUND.getMessage(), 1), response.getMessage());
    }

    @Test
    void update() {

        roleRepository.save(getRoleEntityOne());

        RoleDtoOut response = webTestClient
                .put()
                .uri(PATH_ROLES + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .bodyValue(getRoleDtoInTwo())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(RoleDtoOut.class)
                .returnResult()
                .getResponseBody();

        assertThat(getRoleDtoOutTwo().withId(1L))
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void delete() {

        roleRepository.save(getRoleEntityOne());

        webTestClient.delete()
                .uri(PATH_ROLES + ID)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void delete_Not_Fount() {

        ErrorResponse response = webTestClient
                .delete()
                .uri(PATH_ROLES + ID)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(response);
        assertEquals(ROLE_NOT_FOUND.getCode(), response.getCode());
        assertEquals(String.format(ROLE_NOT_FOUND.getMessage(), 1), response.getMessage());
    }
}