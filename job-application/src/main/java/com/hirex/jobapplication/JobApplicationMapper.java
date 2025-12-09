package com.hirex.jobapplication;

import com.hirex.jobapplication.domain.JobApplication;
import com.hirex.jobapplication.dto.JobApplicationDto;
import com.hirex.jobapplication.dto.JobApplicationResumoDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {

    JobApplicationResumoDto toJobApplicationResumoDto(JobApplication jobApplication);

    List<JobApplicationResumoDto> toJobApplicationsResumoDto(Iterable<JobApplication> jobApplications);

    JobApplicationDto toJobApplicationDto(JobApplication jobApplication);

    JobApplication toJobApplication(JobApplicationDto dto);

}
