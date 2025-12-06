package com.awdhx.companyms.company.service;


import com.awdhx.companyms.company.entity.Company;

import java.util.List;

public interface CompanyService {

    List<Company> getAllCompanies();

    boolean updateCompany(Company company,Long id);

    void createCompany(Company company);

    Company getCompanyById(Long id);

    boolean deleteCompanyById(Long Id);
}
