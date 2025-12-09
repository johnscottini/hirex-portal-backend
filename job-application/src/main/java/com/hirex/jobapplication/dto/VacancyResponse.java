package com.hirex.jobapplication.dto;

import com.hirex.jobapplication.domain.enums.VacancyStatus;
import lombok.Data;

@Data
public class VacancyResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private VacancyStatus status;
}
