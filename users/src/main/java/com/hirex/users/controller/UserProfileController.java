package com.hirex.users.controller;

import com.hirex.users.dto.UserDto;
import com.hirex.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user-profile")
public class UserProfileController {

    private final UserService userService;

    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        boolean emailVerified = Boolean.TRUE.equals(jwt.getClaim("email_verified"));

        return userService.ensureAndGetByKeycloakIdentity(keycloakId, username, email, emailVerified);
    }
}
