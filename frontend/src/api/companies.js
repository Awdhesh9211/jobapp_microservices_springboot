import axios from "axios";

const api = axios.create({
    baseURL: "/companies",
});

export const getAllCompanies = () => api.get("");
export const getCompanyById = (id) => api.get(`/${id}`);
export const createCompany = (company) => api.post("", company);
export const deleteCompany = (id) => api.delete(`/${id}`);
export const updateCompany = (id, company) => api.put(`/${id}`, company);
