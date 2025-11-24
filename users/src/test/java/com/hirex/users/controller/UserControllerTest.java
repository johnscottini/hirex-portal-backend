package com.hirex.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirex.users.dto.UserDto;
import com.hirex.users.dto.UserResumoDto;
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@Import(UserControllerTest.Config.class)
class UserControllerTest {

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
        return dto;
    }

    @Test
    @DisplayName("GET /api/user should return list of resumo DTOs")
    void findAll_shouldReturnList() throws Exception {
        UserResumoDto r = new UserResumoDto();
        r.setId(1L);
        r.setUsername("john");
        r.setFullName("John Doe");
        r.setEmail("john@ex.com");
        r.setCpf("12345678900");

        Mockito.when(userService.findAll(eq(10), eq(10), eq("username")))
                .thenReturn(List.of(r));

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is("john")))
                .andExpect(jsonPath("$[0].fullName", is("John Doe")));
    }

    @Test
    @DisplayName("GET /api/user/{id} should return user DTO")
    void findById_shouldReturnDto() throws Exception {
        Mockito.when(userService.get(1L)).thenReturn(sampleUserDto());

        mockMvc.perform(get("/api/user/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("john")));
    }

    @Test
    @DisplayName("POST /api/user should create user and return 201")
    void saveUser_shouldReturn201() throws Exception {
        UserDto input = sampleUserDto();
        input.setId(null);
        UserDto output = sampleUserDto();

        Mockito.when(userService.save(any(UserDto.class))).thenReturn(output);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("john")));
    }

    @Test
    @DisplayName("PUT /api/user/{id} should update and return 200")
    void updateUser_shouldReturn200() throws Exception {
        UserDto patch = new UserDto();
        patch.setFullName("New Name");
        UserDto updated = sampleUserDto();
        updated.setFullName("New Name");

        Mockito.when(userService.update(eq(1L), any(UserDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/user/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is("New Name")));
    }

    @Test
    @DisplayName("PUT /api/user/{id} should return 404 when service returns null")
    void updateUser_shouldReturn404WhenNull() throws Exception {
        UserDto patch = new UserDto();
        patch.setFullName("New Name");

        Mockito.when(userService.update(eq(2L), any(UserDto.class))).thenReturn(null);

        mockMvc.perform(put("/api/user/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User not found.")));
    }

    @Test
    @DisplayName("DELETE /api/user/{id} should return 200")
    void deleteUser_shouldReturn200() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User deleted successfully.")));
        Mockito.verify(userService).delete(1L);
    }
}
