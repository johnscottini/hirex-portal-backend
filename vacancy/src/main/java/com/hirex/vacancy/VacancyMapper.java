package com.hirex.vacancy;

import com.hirex.vacancy.domain.Vacancy;
import com.hirex.vacancy.dto.VacancyDto;
import com.hirex.vacancy.dto.VacancyResumoDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VacancyMapper {

    VacancyResumoDto toVacancyResumoDto(Vacancy vacancy);
    List<VacancyResumoDto> toVacanciesResumoDto(Iterable<Vacancy> vacancy);

    VacancyDto toVacancyDto(Vacancy vacancy);

    Vacancy toVacancy(VacancyDto dto);

    void updateVacancyFromDto(VacancyDto vacancyDto, @MappingTarget Vacancy vacancy);
}
