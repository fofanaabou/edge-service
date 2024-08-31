package com.sinignaci.edgeservice.controller;

import com.sinignaci.edgeservice.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping
public class UserController {

    @GetMapping("/user")
    public Mono<User> getUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(authentication -> (OidcUser)authentication.getPrincipal())
                .map(UserController::getUser);
    }

    @GetMapping("/v2/user")
    public Mono<User> getUserUsingPrincipal(@AuthenticationPrincipal OidcUser oidcUser) {
        return Mono.just(getUser(oidcUser));
    }

    private static User getUser(OidcUser oidcUser) {
        return new User(oidcUser.getPreferredUsername(),
                oidcUser.getGivenName(),
                oidcUser.getFamilyName(),
                List.of("employee", "customer"));
    }
}
