package com.hirex.users.controller;

import com.hirex.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUsersController {

    private final UserService userService;

    @PostMapping("/ensure")
    public ResponseEntity<Void> ensure(@AuthenticationPrincipal Jwt jwt) {
        String sub = jwt.getSubject();
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        boolean verified = Boolean.TRUE.equals(jwt.getClaim("email_verified"));
        userService.ensureAndGetByKeycloakIdentity(sub, username, email, verified);
        return ResponseEntity.ok().build();
    }
}
