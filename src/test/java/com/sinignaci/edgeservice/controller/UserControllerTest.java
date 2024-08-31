package com.sinignaci.edgeservice.controller;

import com.sinignaci.edgeservice.config.SecurityConfig;
import com.sinignaci.edgeservice.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Test
    void whenNotAuthenticatedThen401() {
        webTestClient
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenAuthenticatedThenReturnUser() {
        var expectedUser = User.builder()
                .username("jon.snow")
                .firstName("Jon")
                .lastName("Snow")
                .roles(List.of("employee", "customer"))
                .build();

        webTestClient.mutateWith(configureMockOidcLogin(expectedUser))
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> assertEquals(expectedUser, user));
    }

    private SecurityMockServerConfigurers.OidcLoginMutator configureMockOidcLogin(User user) {

        return SecurityMockServerConfigurers.mockOidcLogin()
                .idToken(token -> {
                    token.claim(StandardClaimNames.PREFERRED_USERNAME, user.username());
                    token.claim(StandardClaimNames.GIVEN_NAME, user.firstName());
                    token.claim(StandardClaimNames.FAMILY_NAME, user.lastName());
                    token.claim("roles", user.roles());
                });
    }
}
