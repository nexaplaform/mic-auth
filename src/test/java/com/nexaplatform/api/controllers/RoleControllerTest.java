package com.nexaplatform.api.controllers;

import com.nexaplatform.api.controllers.services.dto.out.ErrorResponse;
import com.nexaplatform.api.controllers.services.dto.out.RoleDtoOut;
import com.nexaplatform.infrastructura.db.postgres.repositories.RoleRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.nexaplatform.domain.errors.Error.ROLE_NOT_FOUND;
import static com.nexaplatform.providers.user.RoleProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoleControllerTest extends BaseIntegration {

    @Autowired
    private RoleRepositoryAdapter roleRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void create() {

        RoleDtoOut response = webTestClient
                .post()
                .uri("/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
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
                .uri("/v1/roles?page=0&size=10&sort=ASC")
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
                .uri("/v1/roles/1")
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
                .uri("/v1/roles/1")
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
                .uri("/v1/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
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
                .uri("/v1/roles/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void delete_Not_Fount() {

        ErrorResponse response = webTestClient
                .delete()
                .uri("/v1/roles/1")
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