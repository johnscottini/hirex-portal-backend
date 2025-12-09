package com.hirex.jobapplication.controller;

import com.hirex.jobapplication.dto.JobApplicationDto;
import com.hirex.jobapplication.dto.JobApplicationResumoDto;
import com.hirex.jobapplication.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService service;

    @GetMapping
    public List<JobApplicationResumoDto> findAll() {
        return service.findAll(10, 10, "tile");
    }

    @GetMapping("{id}")
    public JobApplicationDto findById(@PathVariable String id) {
        return service.get(id);
    }

    @PostMapping
    public JobApplicationDto apply(@RequestParam Long candidateId,
                                   @RequestParam Long vacancyId) {
        return service.apply(candidateId, vacancyId);
    }

    @GetMapping("/candidate/{id}")
    public List<JobApplicationResumoDto> findByCandidate(@PathVariable Long id) {
        return service.getByCandidate(id);
    }

    @GetMapping("/vacancy/{id}")
    public List<JobApplicationResumoDto> findByVacancy(@PathVariable Long id) {
        return service.getByVacancy(id);
    }
}