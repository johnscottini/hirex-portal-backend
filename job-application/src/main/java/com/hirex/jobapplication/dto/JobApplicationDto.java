package com.hirex.jobapplication.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hirex.jobapplication.domain.enums.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobApplicationDto {

    private String id;
    private Long candidateId;
    private Long vacancyId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime appliedDate;
    private ApplicationStatus status;
}
