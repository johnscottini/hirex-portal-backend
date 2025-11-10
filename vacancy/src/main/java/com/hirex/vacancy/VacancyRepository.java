package com.hirex.vacancy;

import com.hirex.vacancy.domain.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
}
