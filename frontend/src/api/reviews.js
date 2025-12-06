import axios from "axios";

const api = axios.create({
    baseURL: "/",
});

export const getReviewsByCompanyId = (companyId) => api.get("/reviews", { params: { companyId } });
export const addReview = (companyId, review) => api.post("/reviews", review, { params: { companyId } });
export const getReviewById = (id) => api.get(`/reviews/${id}`);
export const deleteReview = (id) => api.delete(`/reviews/${id}`);
export const updateReview = (id, review) => api.put(`/reviews/${id}`, review);
