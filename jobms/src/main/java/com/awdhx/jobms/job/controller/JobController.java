package com.awdhx.jobms.job.controller;


import com.awdhx.jobms.job.dto.JobWithCompanyDTO;
import com.awdhx.jobms.job.entity.Job;
import com.awdhx.jobms.job.external.Company;
import com.awdhx.jobms.job.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;


//  GET ALL JOBS
    @GetMapping("")
    public ResponseEntity<List<JobWithCompanyDTO>>  findAll(){
        // ASYNC COMMUNICATION BETWEEN SERVICES
        //Company company=new RestTemplate().getForObject("http://localhost:8081/companies/1", Company.class);


        return ResponseEntity.ok(jobService.findAll());
    }

//  GET JOB BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id){
        Job job=jobService.getJobById(id);
        if(job != null)
            return new ResponseEntity<>(job, HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//  CREATE A JOB
    @PostMapping("")
    public ResponseEntity<String> createJobs(@RequestBody Job job){
        jobService.createJob(job);
        return new ResponseEntity<>("Job Created Sucess",HttpStatus.CREATED);
    }

//  DELETE A JOB BY ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletejobByid(@PathVariable Long id){
        if(jobService.deleteJobById(id))
            return new ResponseEntity<>("deleted successfully!",HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//  UPDATE A JOB BY ID AND DATA
    @PutMapping("/{id}")
    public ResponseEntity<String> updateJobById(@PathVariable Long id,@RequestBody Job updatedJob){
        boolean updated=jobService.updateJobById(id,updatedJob);

        if(updated)
            return new ResponseEntity<>("Job Updated Successfully !",HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


}
