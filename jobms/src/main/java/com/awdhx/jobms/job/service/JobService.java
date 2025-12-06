package com.awdhx.jobms.job.service;



import com.awdhx.jobms.job.dto.JobWithCompanyDTO;
import com.awdhx.jobms.job.entity.Job;

import java.util.List;

public interface JobService {
    List<JobWithCompanyDTO> findAll();

    void createJob(Job job);

    Job getJobById(Long id);

    boolean deleteJobById(Long Id);

    boolean updateJobById(Long Id,Job updatedJob);
}
