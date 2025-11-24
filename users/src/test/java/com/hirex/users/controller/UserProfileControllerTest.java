package com.hirex.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirex.users.dto.UserDto;
import com.hirex.users.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserProfileController.class)
@Import(UserProfileControllerTest.Config.class)
class UserProfileControllerTest {

    @TestConfiguration
    static class Config {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private UserDto sampleUserDto() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setUsername("john");
        dto.setFullName("John Doe");
        dto.setEmail("john@ex.com");
        dto.setCpf("12345678900");
        dto.setBirthDate(LocalDate.of(1990,1,1));
        dto.setKeycloakId("kc-123");
        dto.setEmailVerified(true);
        dto.setEnabled(true);
        return dto;
    }

    @Test
    @DisplayName("GET /api/user-profile/me should JIT-provision and return current user profile")
    void me_shouldProvisionAndReturnProfile() throws Exception {
        Mockito.when(userService.ensureAndGetByKeycloakIdentity(anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn(sampleUserDto());

        mockMvc.perform(get("/api/user-profile/me")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", "kc-123")
                                .claim("preferred_username", "john")
                                .claim("email", "john@ex.com")
                                .claim("email_verified", true)
                                .claim("realm_access", Map.of("roles", List.of("USER")))
                        ))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("john")))
                .andExpect(jsonPath("$.email", is("john@ex.com")));

        Mockito.verify(userService).ensureAndGetByKeycloakIdentity("kc-123", "john", "john@ex.com", true);
    }
}
