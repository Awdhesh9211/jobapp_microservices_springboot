package com.awdhx.jobms.job.dto;

import com.awdhx.jobms.job.entity.Job;
import com.awdhx.jobms.job.external.Company;

public class JobWithCompanyDTO {

    private Job job;
    private Company company;

    public JobWithCompanyDTO() {
    }

    public JobWithCompanyDTO(Job job, Company company) {
        this.job = job;
        this.company = company;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
