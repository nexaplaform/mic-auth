package com.nexaplatform.api.controllers;

import com.nexaplatform.api.controllers.services.dto.in.UserDtoIn;
import com.nexaplatform.api.controllers.services.dto.out.UserDtoOut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.nexaplatform.providers.user.UserProvider.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIT extends BaseIntegration {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    void create() {

        UserDtoIn dtoIn = getUserDtoInOne();

        UserDtoOut expectedDtoOut = webTestClient
                .post()
                .uri(BASE_PATH)
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
    }

    @Test
    void getById() {

        uRepository.save(getUserEntityOne().withId(null));

        UserDtoOut expectedDtoOut = webTestClient
                .get()
                .uri(BASE_PATH + "/1")
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

        uRepository.save(getUserEntityOne().withId(null));

        UserDtoOut expectedDtoOut = webTestClient
                .put()
                .uri(BASE_PATH + "/1")
                .bodyValue(getUserDtoInTwo())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserDtoOut.class)
                .returnResult()
                .getResponseBody();

        assertThat(getUserDtoOutTwo())
                .usingRecursiveComparison()
                .isEqualTo(expectedDtoOut);

    }

    @Test
    void delete() {

        uRepository.save(getUserEntityOne().withId(null));
        webTestClient
                .delete()
                .uri(BASE_PATH + "/1")
                .exchange()
                .expectStatus().isNoContent();

        assertThat(uRepository.existsById(1L)).isFalse();
    }
}