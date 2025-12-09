package com.hirex.jobapplication;

import com.hirex.jobapplication.domain.JobApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobApplicationRepository extends MongoRepository<JobApplication, String> {

    List<JobApplication> findByCandidateId(Long candidateId);

    List<JobApplication> findByVacancyId(Long vacancyId);

    boolean existsByCandidateIdAndVacancyId(Long candidateId, Long vacancyId);
}
