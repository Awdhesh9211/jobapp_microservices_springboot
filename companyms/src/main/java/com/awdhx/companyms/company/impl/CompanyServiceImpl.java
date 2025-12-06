package com.awdhx.companyms.company.impl;

import com.awdhx.companyms.company.entity.Company;
import com.awdhx.companyms.company.repository.CompanyRepository;
import com.awdhx.companyms.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

//  GET ALL COMPANIES
    @Override
    public List<Company> getAllCompanies() {
       return companyRepository.findAll();
    }

//  GET COMPANY BY ID
    @Override
    public Company getCompanyById(Long id) {
    return companyRepository.findById(id).orElse(null);
}

//  CREATE COMPANY
    @Override
    public void createCompany(Company company) {
        companyRepository.save(company);
    }

// DELETE COMPANY
    @Override
    public boolean deleteCompanyById(Long Id) {
        try{
            companyRepository.deleteById(Id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//  UPDATE COMPANY
    @Override
    public boolean updateCompany(Company company,Long id) {
        Optional<Company> companyOptional=companyRepository.findById(id);
        if(companyOptional.isPresent()){
            Company companyToUpdate=companyOptional.get();
            if(company.getName()!= null) companyToUpdate.setName(company.getName());
            if(company.getDescription()!= null) companyToUpdate.setDescription(company.getDescription());
//            if(company.getJobs()!= null) companyToUpdate.setJobs(company.getJobs());
            companyRepository.save(companyToUpdate);
            return true;
        }
        return false;
    }
}
