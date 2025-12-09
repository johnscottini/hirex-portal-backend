package com.hirex.vacancy.dto;

import com.hirex.vacancy.domain.enums.JobFormat;
import com.hirex.vacancy.domain.enums.JobType;
import com.hirex.vacancy.domain.enums.VacancyStatus;
import lombok.Data;

@Data
public class VacancyDto {

    private Long id;
    private String title;
    private String description;
    private String location;
    private JobFormat jobFormat;
    private JobType jobType;
    private VacancyStatus status;
}
