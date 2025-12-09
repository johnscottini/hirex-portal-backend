package com.hirex.vacancy.controller;

import com.hirex.vacancy.dto.VacancyDto;
import com.hirex.vacancy.dto.VacancyResumoDto;
import com.hirex.vacancy.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/vacancy")
public class VacancyController {

    private final VacancyService vacancyService;

    @GetMapping
    public List<VacancyResumoDto> findAll() {
        return vacancyService.findAll(10, 10, "username");
    }

    @GetMapping("{id}")
    public VacancyDto findById(@PathVariable Long id) {
        return vacancyService.get(id);
    }

    @PostMapping
    public ResponseEntity<VacancyDto> saveVacancy(@RequestBody VacancyDto userDto) {
        VacancyDto savedUser = vacancyService.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateVacancy(@PathVariable(value = "id") Long id,
                                             @RequestBody VacancyDto userDto) {
        VacancyDto updatedUser = vacancyService.update(id, userDto);
        if (Objects.isNull(updatedUser)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vacancy not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVacancy(@PathVariable(value = "id") Long id) {
        vacancyService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Vacancy deleted successfully.");
    }
}
