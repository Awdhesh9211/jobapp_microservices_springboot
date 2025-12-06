import axios from "axios";

const api = axios.create({
    baseURL: "/jobs",
});

export const getAllJobs = () => api.get("");
export const getJobById = (id) => api.get(`/${id}`);
export const createJob = (job) => api.post("", job);
export const deleteJob = (id) => api.delete(`/${id}`);
export const updateJob = (id, job) => api.put(`/${id}`, job);
