package com.hirex.jobapplication.service;

import com.hirex.common.exception.AlreadyAppliedException;
import com.hirex.common.exception.UserNotFoundException;
import com.hirex.common.exception.VacancyNotFoundException;
import com.hirex.common.utils.PageUtils;
import com.hirex.jobapplication.JobApplicationMapper;
import com.hirex.jobapplication.JobApplicationRepository;
import com.hirex.jobapplication.client.UserClient;
import com.hirex.jobapplication.client.VacancyClient;
import com.hirex.jobapplication.domain.JobApplication;
import com.hirex.jobapplication.domain.enums.VacancyStatus;
import com.hirex.jobapplication.dto.JobApplicationDto;
import com.hirex.jobapplication.dto.JobApplicationResumoDto;
import com.hirex.jobapplication.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository repository;
    private final UserClient userClient;
    private final VacancyClient vacancyClient;
    private final JobApplicationMapper jobApplicationMapper;

    public JobApplicationDto apply(Long candidateId, Long vacancyId) {
        if (repository.existsByCandidateIdAndVacancyId(candidateId, vacancyId)) {
            throw new AlreadyAppliedException("Candidate already applied to this vacancy.");
        }

        UserResponse user = userClient.getUserById(candidateId);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("User not found with id: " + candidateId);
        }

        var vacancy = vacancyClient.getVacancyById(vacancyId);
        if (Objects.isNull(vacancy)) {
            throw new VacancyNotFoundException("Vacancy not found with id: " + vacancyId);
        }

        if (!Objects.equals(vacancy.getStatus(), VacancyStatus.OPEN)) {
            throw new RuntimeException("Vacancy is not open for applications: " + vacancyId);
        }

        if (!user.getEnabled()) {
            throw new RuntimeException("User is disabled: " + candidateId);
        }

        var app = JobApplication.create(candidateId, vacancyId);
        return jobApplicationMapper.toJobApplicationDto(repository.save(app));
    }

    public List<JobApplicationResumoDto> getByCandidate(Long candidateId) {
        var apps = repository.findByCandidateId(candidateId);
        return jobApplicationMapper.toJobApplicationsResumoDto(apps);
    }

    public List<JobApplicationResumoDto> getByVacancy(Long vacancyId) {
        var apps = repository.findByVacancyId(vacancyId);
        return jobApplicationMapper.toJobApplicationsResumoDto(apps);
    }

    public List<JobApplicationResumoDto> findAll(Integer pageIndex, Integer pageSize, String sortField) {
        var applications = repository.findAll(PageUtils.page(pageIndex, pageSize, sortField));

        return jobApplicationMapper.toJobApplicationsResumoDto(applications);
    }

    public JobApplicationDto get(String id) {
        var application = repository.findById(id).orElse(null);
        return jobApplicationMapper.toJobApplicationDto(application);
    }
}