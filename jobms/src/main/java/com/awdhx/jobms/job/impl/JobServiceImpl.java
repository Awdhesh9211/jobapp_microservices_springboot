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
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyClient companyClient;

    @Autowired
    private ReviewClient reviewClient;

    // CONVERTER - Updated with circuit breaker
    private JobDTO convertToDto(Job job) {
        // Check if job has a company assigned
        if (job.getComapnyId() == null) {
            Company fallbackCompany = createNoCompanyFallback();
            return JobMapper.mapToJobWithCompanyDto(job, fallbackCompany, Collections.emptyList());
        }

        Company company = getCompanyWithBreaker(job.getComapnyId());
        List<Review> reviews = getReviewsWithBreaker(company.getId());

        return JobMapper.mapToJobWithCompanyDto(job, company, reviews);
    }

    // ALL JOB
    @Override
    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream()
                .map(job -> {
                    // Check if job has a company assigned
                    if (job.getComapnyId() == null) {
                        Company fallbackCompany = createNoCompanyFallback();
                        return JobMapper.mapToJobWithCompanyDto(job, fallbackCompany, Collections.emptyList());
                    }

                    // Using Resilience4j Circuit Breaker with OpenFeign
                    Company company = getCompanyWithBreaker(job.getComapnyId());
                    List<Review> reviews = getReviewsWithBreaker(company.getId());

                    return JobMapper.mapToJobWithCompanyDto(job, company, reviews);
                })
                .collect(Collectors.toList());
    }

    // Circuit Breaker for Company Service
    @CircuitBreaker(name = "companyBreaker", fallbackMethod = "companyFallback")
    @Retry(name = "companyRetry")
    public Company getCompanyWithBreaker(Long companyId) {
        return companyClient.getCompany(companyId);
    }

    public Company companyFallback(Long companyId, Throwable ex) {
        System.err.println("Company service fallback triggered for companyId: " + companyId + ", Error: " + ex.getMessage());
        Company fallback = new Company();
        fallback.setId(companyId);
        fallback.setName("Company Service Unavailable");
        fallback.setDescription("Unable to fetch company details at this time. Please try again later.");
        return fallback;
    }

    // Circuit Breaker for Review Service
//    @CircuitBreaker(name = "reviewBreaker", fallbackMethod = "reviewFallback")
//    @Retry(name = "reviewRetry")
    @RateLimiter(name = "reviewBreaker", fallbackMethod = "reviewFallback")
    public List<Review> getReviewsWithBreaker(Long companyId) {
        return reviewClient.getReviews(companyId);
    }

    public List<Review> reviewFallback(Long companyId, Throwable ex) {
        System.err.println("Review service fallback triggered for companyId: " + companyId + ", Error: " + ex.getMessage());
        return Collections.emptyList();
    }

    // Helper method to create fallback company when job has no company assigned
    private Company createNoCompanyFallback() {
        Company fallback = new Company();
        fallback.setId(null);
        fallback.setName("No Company Assigned");
        fallback.setDescription("This job is not associated with any company.");
        return fallback;
    }

    // GET A JOB
    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            return null;
        }
        return convertToDto(job);
    }

    // CREATE A JOB
    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    // DELETE A JOB
    @Override
    public boolean deleteJobById(Long id) {
        try {
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting job with id: " + id + ", Error: " + e.getMessage());
            return false;
        }
    }

    // UPDATE A JOB
    @Override
    public boolean updateJobById(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            if (updatedJob.getTitle() != null) job.setTitle(updatedJob.getTitle());
            if (updatedJob.getDescription() != null) job.setDescription(updatedJob.getDescription());
            if (updatedJob.getMinSalary() != null) job.setMinSalary(updatedJob.getMinSalary());
            if (updatedJob.getMaxSalary() != null) job.setMaxSalary(updatedJob.getMaxSalary());
            if (updatedJob.getLocation() != null) job.setLocation(updatedJob.getLocation());
            if (updatedJob.getComapnyId() != null) job.setComapnyId(updatedJob.getComapnyId());
            jobRepository.save(job);
            return true;
        }
        return false;
    }
}



//package com.awdhx.jobms.job.impl;
//
//
//import com.awdhx.jobms.job.client.CompanyClient;
//import com.awdhx.jobms.job.client.ReviewClient;
//import com.awdhx.jobms.job.dto.JobDTO;
//import com.awdhx.jobms.job.entity.Job;
//import com.awdhx.jobms.job.external.Company;
//import com.awdhx.jobms.job.external.Review;
//import com.awdhx.jobms.job.mapper.JobMapper;
//import com.awdhx.jobms.job.repository.JobRepository;
//import com.awdhx.jobms.job.service.JobService;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class JobServiceImpl implements JobService {
//
//    @Autowired
//    private JobRepository jobRepository;
//
////    @Autowired
//    private RestTemplate restTemplate=new RestTemplate();
//
//
//    //OPEN FEIGN CLIENTS
//    @Autowired
//    private CompanyClient companyClient;
//
//    @Autowired
//    private ReviewClient reviewClient;
//
//    // CONVERTER
//    private JobDTO convertToDto(Job job){
//        // WE ARE NOW USING OPEN FIEGN
////        Company company=restTemplate.getForObject("http://COMPANYMS:8081/companies/"+job.getComapnyId(), Company.class);
////        List<Review> reviews=restTemplate.exchange(
////                "http://REVIEWMS:8083/review?companyId=" + company.getId(),
////                HttpMethod.GET,
////                null,
////                new ParameterizedTypeReference<List<Review>>() {
////                }
////        ).getBody();
//        Company company=companyClient.getCompany(job.getComapnyId());
//        List<Review> reviews=reviewClient.getReviews(company.getId());
//
//        return JobMapper.mapToJobWithCompanyDto(job,company,reviews);
//    }
//
//    // ALL JOB
//    @Override
//    public List<JobDTO> findAll() {
//        List<Job> jobs = jobRepository.findAll();
//        return jobs.stream()
//                .map(job -> {
//                    // Using Resilience4j Circuit Breaker with OpenFeign
//                    Company company = getCompanyWithBreaker(job.getComapnyId());
//                    List<Review> reviews = getReviewsWithBreaker(company.getId());
//                    return JobMapper.mapToJobWithCompanyDto(job, company, reviews);
//                })
//                .collect(Collectors.toList());
//    }
//
//    public Company companyFallback(Long companyId, Throwable ex) {
//        // Fallback logic
//        Company fallback = new Company();
//        fallback.setId(companyId);
//        fallback.setName("Company service temporarily unavailable");
//        fallback.setDescription("");
//        return fallback;
//    }
//
//    @CircuitBreaker(name = "companyBreaker", fallbackMethod = "companyFallback")
//    public Company getCompanyWithBreaker(Long companyId) {
//        return companyClient.getCompany(companyId);
//    }
//
//    public List<Review> reviewFallback(Long companyId, Throwable ex) {
//        // Return empty list if Review service is down
//        return Collections.emptyList();
//    }
//
//    @CircuitBreaker(name = "reviewBreaker", fallbackMethod = "reviewFallback")
//    public List<Review> getReviewsWithBreaker(Long companyId) {
//        return reviewClient.getReviews(companyId);
//    }
//
//
//
////  GET A JOB
//    @Override
//    public JobDTO getJobById(Long id) {
//        Job job= jobRepository.findById(id).orElse(null);
//        return convertToDto(job);
//    }
//
////  CREATE A JOB
//    @Override
//    public void createJob(Job job) {
//        jobRepository.save(job);
//    }
//
////  DELETE A JOB
//    @Override
//    public boolean deleteJobById(Long Id) {
//        try{
//            jobRepository.deleteById(Id);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
////  UPDATE A JOB
//    @Override
//    public boolean updateJobById(Long Id, Job updatedJob) {
//
//        Optional<Job> jobOptional=jobRepository.findById(Id);
//        if(jobOptional.isPresent()){
//            Job job=jobOptional.get();
//            if(updatedJob.getTitle()!= null) job.setTitle(updatedJob.getTitle());
//            if(updatedJob.getDescription()!= null) job.setDescription(updatedJob.getDescription());
//            if(updatedJob.getMinSalary()!= null) job.setMinSalary(updatedJob.getMinSalary());
//            if(updatedJob.getMaxSalary()!= null) job.setMaxSalary(updatedJob.getMaxSalary());
//            if(updatedJob.getLocation()!= null) job.setLocation(updatedJob.getLocation());
//            jobRepository.save(job);
//            return true;
//        }
//        return false;
//    }
//}
