package com.hirex.vacancy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirex.vacancy.dto.VacancyDto;
import com.hirex.vacancy.dto.VacancyResumoDto;
import com.hirex.vacancy.service.VacancyService;
import com.hirex.vacancy.domain.enums.JobFormat;
import com.hirex.vacancy.domain.enums.JobType;
import com.hirex.vacancy.domain.enums.VacancyStatus;
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

@WebMvcTest(controllers = VacancyController.class)
@Import(VacancyControllerTest.Config.class)
class VacancyControllerTest {

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
        public VacancyService vacancyService() {
            return Mockito.mock(VacancyService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VacancyService vacancyService;

    private VacancyDto sampleVacancyDto() {
        VacancyDto dto = new VacancyDto();
        dto.setId(1L);
        dto.setTitle("Java Developer");
        dto.setDescription("Backend position");
        dto.setLocation("Remote");
        dto.setJobFormat(JobFormat.REMOTE);
        dto.setJobType(JobType.FULL_TIME);
        dto.setStatus(VacancyStatus.OPEN);
        return dto;
    }

    @Test
    @DisplayName("GET /api/vacancy should return list of resumo DTOs")
    void findAll_shouldReturnList() throws Exception {
        VacancyResumoDto r = new VacancyResumoDto();
        r.setId(1L);
        r.setTitle("Java Developer");
        r.setLocation("Remote");
        r.setJobFormat(JobFormat.REMOTE);
        r.setJobType(JobType.FULL_TIME);

        Mockito.when(vacancyService.findAll(eq(10), eq(10), eq("username")))
                .thenReturn(List.of(r));

        mockMvc.perform(get("/api/vacancy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Java Developer")))
                .andExpect(jsonPath("$[0].location", is("Remote")));
    }

    @Test
    @DisplayName("GET /api/vacancy/{id} should return vacancy DTO")
    void findById_shouldReturnDto() throws Exception {
        Mockito.when(vacancyService.get(1L)).thenReturn(sampleVacancyDto());

        mockMvc.perform(get("/api/vacancy/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Java Developer")));
    }

    @Test
    @DisplayName("POST /api/vacancy should create vacancy and return 201")
    void saveVacancy_shouldReturn201() throws Exception {
        VacancyDto input = sampleVacancyDto();
        input.setId(null);
        VacancyDto output = sampleVacancyDto();

        Mockito.when(vacancyService.save(any(VacancyDto.class))).thenReturn(output);

        mockMvc.perform(post("/api/vacancy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Java Developer")));
    }

    @Test
    @DisplayName("PUT /api/vacancy/{id} should update and return 200")
    void updateVacancy_shouldReturn200() throws Exception {
        VacancyDto patch = new VacancyDto();
        patch.setTitle("New Title");
        VacancyDto updated = sampleVacancyDto();
        updated.setTitle("New Title");

        Mockito.when(vacancyService.update(eq(1L), any(VacancyDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/vacancy/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Title")));
    }

    @Test
    @DisplayName("PUT /api/vacancy/{id} should return 404 when service returns null")
    void updateVacancy_shouldReturn404WhenNull() throws Exception {
        VacancyDto patch = new VacancyDto();
        patch.setTitle("New Title");

        Mockito.when(vacancyService.update(eq(2L), any(VacancyDto.class))).thenReturn(null);

        mockMvc.perform(put("/api/vacancy/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Vacancy not found.")));
    }

    @Test
    @DisplayName("DELETE /api/vacancy/{id} should return 200")
    void deleteVacancy_shouldReturn200() throws Exception {
        mockMvc.perform(delete("/api/vacancy/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Vacancy deleted successfully.")));
        Mockito.verify(vacancyService).delete(1L);
    }
}
