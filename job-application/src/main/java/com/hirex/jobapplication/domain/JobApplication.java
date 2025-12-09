package com.hirex.jobapplication.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hirex.jobapplication.domain.enums.ApplicationStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "job_applications")
public class JobApplication {

    @Id
    private String id;

    private Long candidateId;
    private Long vacancyId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime appliedDate;

    private ApplicationStatus status;

    public static JobApplication create(Long candidateId, Long vacancyId) {
        return JobApplication.builder()
                .candidateId(candidateId)
                .vacancyId(vacancyId)
                .appliedDate(LocalDateTime.now())
                .status(ApplicationStatus.APPLIED)
                .build();
    }
}
