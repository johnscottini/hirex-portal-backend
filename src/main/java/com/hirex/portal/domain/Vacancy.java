package com.hirex.portal.domain;

import com.hirex.portal.domain.enums.JobFormat;
import com.hirex.portal.domain.enums.JobType;
import com.hirex.portal.domain.enums.VacancyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vacancy", schema = "public")
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vacancy_seq")
    @SequenceGenerator(name = "vacancy_seq", sequenceName = "vacancy_seq", allocationSize = 1)
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name="job_format")
    private JobFormat jobFormat;

    @Enumerated(EnumType.STRING)
    @Column(name="job_type")
    private JobType jobType;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private VacancyStatus status;
}
