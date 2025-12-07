package com.awdhx.jobms.job.impl;


import com.awdhx.jobms.job.client.CompanyClient;
import com.awdhx.jobms.job.client.ReviewClient;
import com.awdhx.jobms.job.dto.JobDTO;
import com.awdhx.jobms.job.entity.Job;
import com.awdhx.jobms.job.external.Company;
import com.awdhx.jobms.job.external.Review;
import com.awdhx.jobms.job.mapper.JobMapper;
import com.awdhx.jobms.job.repository.JobRepository;
import com.awdhx.jobms.job.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RestTemplate restTemplate;

    //OPEN FEIGN CLIENTS
    @Autowired
    private CompanyClient companyClient;

    @Autowired
    private ReviewClient reviewClient;

    // CONVERTER
    private JobDTO convertToDto(Job job){
        // WE ARE NOW USING OPEN FIEGN
//        Company company=restTemplate.getForObject("http://COMPANYMS:8081/companies/"+job.getComapnyId(), Company.class);
//        List<Review> reviews=restTemplate.exchange(
//                "http://REVIEWMS:8083/review?companyId=" + company.getId(),
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Review>>() {
//                }
//        ).getBody();
        Company company=companyClient.getCompany(job.getComapnyId());
        List<Review> reviews=reviewClient.getReviews(company.getId());

        return JobMapper.mapToJobWithCompanyDto(job,company,reviews);
    }

    // ALL JOB
    @Override
    public List<JobDTO> findAll() {
        List<Job> jobs=jobRepository.findAll();
        return jobs.stream()
                .map(job -> {
                    // WE ARE USING OPEN FEIGN
//                    Company company=restTemplate.getForObject("http://COMPANYMS:8081/companies/"+job.getComapnyId(), Company.class);
//                    List<Review> reviews=restTemplate.exchange(
//                            "http://REVIEWMS:8083/reviews?companyId=" + company.getId(),
//                            HttpMethod.GET,
//                            null,
//                            new ParameterizedTypeReference<List<Review>>() {
//                            }
//                    ).getBody();
                    Company company=companyClient.getCompany(job.getComapnyId());
                    List<Review> reviews=reviewClient.getReviews(company.getId());
                    return JobMapper.mapToJobWithCompanyDto(job,company,reviews);
                }).collect(Collectors.toList());
    }

//  GET A JOB
    @Override
    public JobDTO getJobById(Long id) {
        Job job= jobRepository.findById(id).orElse(null);
        return convertToDto(job);
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
