package com.hirex.jobapplication.client;

import com.hirex.jobapplication.dto.VacancyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vacancy-service")
public interface VacancyClient {

    @GetMapping("api/vacancy/{id}")
    VacancyResponse getVacancyById(@PathVariable Long id);
}