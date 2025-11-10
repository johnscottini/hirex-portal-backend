package com.hirex.vacancy.dto;

import com.hirex.vacancy.domain.enums.JobFormat;
import com.hirex.vacancy.domain.enums.JobType;
import lombok.Data;

@Data
public class VacancyResumoDto {
    private Long id;
    private String title;
    private String location;
    private JobFormat jobFormat;
    private JobType jobType;
}
