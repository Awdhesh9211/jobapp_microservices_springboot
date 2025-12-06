package com.awdhx.jobms.job.impl;


import com.awdhx.jobms.job.dto.JobWithCompanyDTO;
import com.awdhx.jobms.job.entity.Job;
import com.awdhx.jobms.job.external.Company;
import com.awdhx.jobms.job.repository.JobRepository;
import com.awdhx.jobms.job.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RestTemplate restTemplate;


    // ALL JOB
    @Override
    public List<JobWithCompanyDTO> findAll() {
        List<Job> jobs=jobRepository.findAll();
        return jobs.stream()
                .map(job -> {
                    Company company=restTemplate.getForObject("http://localhost:8081/companies/"+job.getComapnyId(), Company.class);
                    return new JobWithCompanyDTO(job,company);
                }).collect(Collectors.toList());
    }

//  GET A JOB
    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

//  CREATE A JOB
    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

//  DELETE A JOB
    @Override
    public boolean deleteJobById(Long Id) {
        try{
            jobRepository.deleteById(Id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//  UPDATE A JOB
    @Override
    public boolean updateJobById(Long Id, Job updatedJob) {

        Optional<Job> jobOptional=jobRepository.findById(Id);
        if(jobOptional.isPresent()){
            Job job=jobOptional.get();
            if(updatedJob.getTitle()!= null) job.setTitle(updatedJob.getTitle());
            if(updatedJob.getDescription()!= null) job.setDescription(updatedJob.getDescription());
            if(updatedJob.getMinSalary()!= null) job.setMinSalary(updatedJob.getMinSalary());
            if(updatedJob.getMaxSalary()!= null) job.setMaxSalary(updatedJob.getMaxSalary());
            if(updatedJob.getLocation()!= null) job.setLocation(updatedJob.getLocation());
            jobRepository.save(job);
            return true;
        }
        return false;
    }
}
