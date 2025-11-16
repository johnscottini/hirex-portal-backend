package com.hirex.vacancy.service;

import com.hirex.common.utils.PageUtils;
import com.hirex.vacancy.VacancyMapper;
import com.hirex.vacancy.VacancyRepository;
import com.hirex.vacancy.dto.VacancyDto;
import com.hirex.vacancy.dto.VacancyResumoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyService {

    private final VacancyMapper vacancyMapper;
    private final VacancyRepository vacancyRepository;

    public List<VacancyResumoDto> findAll(Integer pageIndex, Integer pageSize, String sortField) {

        var vacancies = vacancyRepository.findAll(PageUtils.page(pageIndex, pageSize, "title"));

        return vacancyMapper.toVacanciesResumoDto(vacancies);
    }

    public VacancyDto get(Long id) {
        var vacancies = vacancyRepository.findById(id).orElse(null);
        return vacancyMapper.toVacancyDto(vacancies);

    }

    public VacancyDto save(VacancyDto vacancyDto) {
        var vacancyEntity = vacancyMapper.toVacancy(vacancyDto);
        var vacancySaved = vacancyRepository.save(vacancyEntity);
        return vacancyMapper.toVacancyDto(vacancySaved);
    }

    public VacancyDto update(Long id, VacancyDto vacancyDto) {
        var vacancyToUpdate = vacancyRepository.findById(id).orElse(null);

        vacancyMapper.updateVacancyFromDto(vacancyDto, vacancyToUpdate);
        var updatedUser = vacancyRepository.save(vacancyToUpdate);
        return vacancyMapper.toVacancyDto(updatedUser);
    }

    public void delete(Long id) {
        var vacancy = vacancyRepository.findById(id);
        vacancyRepository.deleteById(id);
    }
}
