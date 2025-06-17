package com.nexaplatform.api.controllers;

import com.nexaplatform.api.controllers.services.dto.in.UserDtoIn;
import com.nexaplatform.api.controllers.services.dto.out.ErrorResponse;
import com.nexaplatform.api.controllers.services.dto.out.UserDtoOut;
import com.nexaplatform.infrastructura.db.postgres.entities.RoleEntity;
import com.nexaplatform.infrastructura.db.postgres.repositories.RoleRepositoryAdapter;
import com.nexaplatform.infrastructura.db.postgres.repositories.UserRepositoryAdapter;
import com.nexaplatform.shared.BaseIntegration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.nexaplatform.domain.errors.Error.USER_NOT_FOUND;
import static com.nexaplatform.providers.authentication.AuthenticationMethodProvider.ROLE_ADMIN;
import static com.nexaplatform.providers.user.RoleProvider.getRoleEntityOne;
import static com.nexaplatform.providers.user.RoleProvider.getRoleEntityTwo;
import static com.nexaplatform.providers.user.UserProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest extends BaseIntegration {

    private static final String ID = "/1";
    public static final String PATH_USERS = "v1/users";

    @Autowired
    protected UserRepositoryAdapter uRepository;
    @Autowired
    protected RoleRepositoryAdapter roleRepository;
    @Autowired
    private WebTestClient webTestClient;

    private List<RoleEntity> getRoles() {
        return roleRepository.saveAll(List.of(
                getRoleEntityOne(),
                getRoleEntityTwo())
        );
    }

    @Test
    void create() {
        this.getRoles();
        UserDtoIn dtoIn = getUserDtoInOne();
        UserDtoOut expectedDtoOut = webTestClient
                .post()
                .uri(PATH_USERS)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dtoIn)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserDtoOut.class)
                .returnResult()
                .getResponseBody();

        assertThat(getUserDtoOutOne())
                .usingRecursiveComparison()
                .isEqualTo(expectedDtoOut);
    }

    @Test
    void getPaginated() {

        List<RoleEntity> roles = this.getRoles();
        uRepository.saveAll(List.of(
                getUserEntityOne().withRoles(roles),
                getUserEntityTwo().withRoles(roles)
        ));

        List<UserDtoOut> expectedDtoOut = webTestClient
                .get()
                .uri(PATH_USERS + "?page=0&size=10&sort=ASC")
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(UserDtoOut.class)
                .returnResult()
                .getResponseBody();

        assertThat(List.of(getUserDtoOutOne(), getUserDtoOutTwo()))
                .usingRecursiveComparison()
                .isEqualTo(expectedDtoOut);
    }

    @Test
    void getById() {

        List<RoleEntity> roles = this.getRoles();
        uRepository.save(getUserEntityOne().withRoles(roles));

        UserDtoOut expectedDtoOut = webTestClient
                .get()
                .uri(PATH_USERS + ID)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserDtoOut.class)
                .returnResult()
                .getResponseBody();

        assertThat(getUserDtoOutOne())
                .usingRecursiveComparison()
                .isEqualTo(expectedDtoOut);

    }

    @Test
    void update() {

        List<RoleEntity> roles = this.getRoles();
        uRepository.save(getUserEntityOne().withRoles(roles));

        UserDtoOut expectedDtoOut = webTestClient
                .put()
                .uri(PATH_USERS + ID)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .bodyValue(getUserDtoInTwo())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserDtoOut.class)
                .returnResult()
                .getResponseBody();

        assertThat(getUserDtoOutTwo().withId(1L))
                .usingRecursiveComparison()
                .isEqualTo(expectedDtoOut);
    }

    @Test
    void delete() {

        List<RoleEntity> roles = this.getRoles();
        uRepository.save(getUserEntityOne().withRoles(roles));
        webTestClient
                .delete()
                .uri(PATH_USERS + ID)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .exchange()
                .expectStatus().isNoContent();

        assertThat(uRepository.existsById(1L)).isFalse();
    }

    @Test
    void delete_not_found() {

        Long id = 100L;
        List<RoleEntity> roles = this.getRoles();
        uRepository.save(getUserEntityOne().withRoles(roles));
        ErrorResponse errorResponse = webTestClient
                .delete()
                .uri(PATH_USERS + "/" + id)
                .header(HttpHeaders.AUTHORIZATION, getToken(List.of(ROLE_ADMIN)))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorResponse);
        assertEquals(USER_NOT_FOUND.getCode(), errorResponse.getCode());
        assertEquals(String.format(USER_NOT_FOUND.getMessage(), id), errorResponse.getMessage());
    }
}