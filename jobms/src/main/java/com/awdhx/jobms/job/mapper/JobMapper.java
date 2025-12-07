package com.awdhx.jobms.job.mapper;

import com.awdhx.jobms.job.dto.JobDTO;
import com.awdhx.jobms.job.entity.Job;
import com.awdhx.jobms.job.external.Company;
import com.awdhx.jobms.job.external.Review;

import java.util.List;

public class JobMapper {
    public static JobDTO mapToJobWithCompanyDto(Job job, Company company, List<Review> reviews){
        JobDTO jobWithCompanyDTO  = new JobDTO();
        jobWithCompanyDTO.setId(job.getId());
        jobWithCompanyDTO.setDescription(job.getDescription());
        jobWithCompanyDTO.setTitle(job.getTitle());
        jobWithCompanyDTO.setLocation(job.getLocation());
        jobWithCompanyDTO.setMaxSalary(job.getMaxSalary());
        jobWithCompanyDTO.setMinSalary(job.getMinSalary());
        jobWithCompanyDTO.setCompany(company);
        jobWithCompanyDTO.setReviews(reviews);
        return jobWithCompanyDTO;
    }
}
