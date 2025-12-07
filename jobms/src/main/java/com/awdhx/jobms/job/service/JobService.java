package com.awdhx.jobms.job.service;



import com.awdhx.jobms.job.dto.JobDTO;
import com.awdhx.jobms.job.entity.Job;

import java.util.List;

public interface JobService {
    List<JobDTO> findAll();

    void createJob(Job job);

    JobDTO getJobById(Long id);

    boolean deleteJobById(Long Id);

    boolean updateJobById(Long Id,Job updatedJob);
}
