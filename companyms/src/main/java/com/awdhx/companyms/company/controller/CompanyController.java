package com.awdhx.companyms.company.controller;

import com.awdhx.companyms.company.entity.Company;
import com.awdhx.companyms.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;


//  GET ALL COMPANY
    @GetMapping("")
    public ResponseEntity<List<Company>> getAllCompanies(){
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

//  GET COMPANY BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id){
        Company company=companyService.getCompanyById(id);
        if(company != null)
            return new ResponseEntity<>(company, HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//  CREATE COMPANY
    @PostMapping("")
    public ResponseEntity<String> createCompany(@RequestBody Company company){
        companyService.createCompany(company);
        return new ResponseEntity<>("Company Created Sucess", HttpStatus.CREATED);
    }

//  DELETE COMPANY BY ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompanyByid(@PathVariable Long id){
        if(companyService.deleteCompanyById(id))
            return new ResponseEntity<>("deleted successfully!",HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//  UPDATE COMPANY BY ID
    @PutMapping("/{id}")
    public ResponseEntity<String> updateJobById(@PathVariable Long id,@RequestBody Company company){
        boolean updated=companyService.updateCompany(company,id);

        if(updated)
            return new ResponseEntity<>("Job Updated Successfully !",HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }




}
