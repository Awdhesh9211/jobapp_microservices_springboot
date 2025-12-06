import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getJobById } from "@/api/jobs";
import { getCompanyById } from "@/api/companies";
import { getReviewsByCompanyId, addReview } from "@/api/reviews";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Skeleton } from "@/components/ui/skeleton";
import { MapPin, DollarSign, Building2, Star, User } from "lucide-react";

export default function JobDetailsPage() {
    const { id } = useParams();
    const [job, setJob] = useState(null);
    const [company, setCompany] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);

    // Review Form
    const [openReview, setOpenReview] = useState(false);
    const [reviewData, setReviewData] = useState({ title: "", description: "", rating: "" });

    useEffect(() => {
        fetchData();
    }, [id]);

    const fetchData = async () => {
        try {
            const jobRes = await getJobById(id);
            const jobData = jobRes.data;
            setJob(jobData);

            if (jobData.comapnyId) {
                const companyRes = await getCompanyById(jobData.comapnyId);
                setCompany(companyRes.data);

                const reviewsRes = await getReviewsByCompanyId(jobData.comapnyId);
                setReviews(reviewsRes.data);
            }
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const handleReviewSubmit = async (e) => {
        e.preventDefault();
        try {
            await addReview(job.comapnyId, reviewData);
            setOpenReview(false);
            setReviewData({ title: "", description: "", rating: "" });
            // Refresh reviews
            const reviewsRes = await getReviewsByCompanyId(job.comapnyId);
            setReviews(reviewsRes.data);
        } catch (error) {
            console.error(error);
        }
    };

    if (loading) {
        return (
            <div className="space-y-6">
                <Skeleton className="h-12 w-1/2" />
                <Skeleton className="h-[200px] w-full" />
                <Skeleton className="h-[200px] w-full" />
            </div>
        )
    }

    if (!job) return <div>Job not found</div>;

    return (
        <div className="space-y-8 max-w-4xl mx-auto">
            {/* Job Header */}
            <div className="space-y-4">
                <h1 className="text-4xl font-bold">{job.title}</h1>
                <div className="flex gap-6 text-muted-foreground">
                    <span className="flex items-center gap-2"><MapPin className="w-5 h-5" /> {job.location}</span>
                    <span className="flex items-center gap-2 text-green-600"><DollarSign className="w-5 h-5" /> {job.minSalary} - {job.maxSalary}</span>
                </div>
            </div>

            {/* Job Description */}
            <Card>
                <CardHeader>
                    <CardTitle>Job Description</CardTitle>
                </CardHeader>
                <CardContent>
                    <p className="whitespace-pre-line leading-relaxed text-muted-foreground">
                        {job.description}
                    </p>
                </CardContent>
            </Card>

            {/* Company Info */}
            {company && (
                <Card className="bg-secondary/20">
                    <CardHeader className="flex flex-row items-center gap-4">
                        <div className="p-3 bg-secondary rounded-full">
                            <Building2 className="w-8 h-8" />
                        </div>
                        <div>
                            <CardTitle>{company.name}</CardTitle>
                            <CardDescription>About the company</CardDescription>
                        </div>
                    </CardHeader>
                    <CardContent>
                        <p className="text-muted-foreground">{company.description}</p>
                    </CardContent>
                </Card>
            )}

            {/* Reviews Section */}
            <div className="space-y-6">
                <div className="flex justify-between items-center">
                    <h2 className="text-2xl font-bold">Company Reviews</h2>
                    <Dialog open={openReview} onOpenChange={setOpenReview}>
                        <DialogTrigger asChild>
                            <Button variant="outline">Write a Review</Button>
                        </DialogTrigger>
                        <DialogContent>
                            <DialogHeader>
                                <DialogTitle>Review {company?.name}</DialogTitle>
                            </DialogHeader>
                            <form onSubmit={handleReviewSubmit} className="space-y-4">
                                <div className="space-y-2">
                                    <Label>Title</Label>
                                    <Input
                                        value={reviewData.title}
                                        onChange={(e) => setReviewData({ ...reviewData, title: e.target.value })}
                                        required
                                    />
                                </div>
                                <div className="space-y-2">
                                    <Label>Rating</Label>
                                    <Select
                                        value={String(reviewData.rating)}
                                        onValueChange={(val) => setReviewData({ ...reviewData, rating: val })}
                                    >
                                        <SelectTrigger>
                                            <SelectValue placeholder="Select Rating" />
                                        </SelectTrigger>
                                        <SelectContent>
                                            {[1, 2, 3, 4, 5].map(r => (
                                                <SelectItem key={r} value={String(r)}>{r} Stars</SelectItem>
                                            ))}
                                        </SelectContent>
                                    </Select>
                                </div>
                                <div className="space-y-2">
                                    <Label>Description</Label>
                                    <Textarea
                                        value={reviewData.description}
                                        onChange={(e) => setReviewData({ ...reviewData, description: e.target.value })}
                                        required
                                    />
                                </div>
                                <Button type="submit" className="w-full">Submit Review</Button>
                            </form>
                        </DialogContent>
                    </Dialog>
                </div>

                <div className="grid gap-4">
                    {reviews.length > 0 ? reviews.map(review => (
                        <Card key={review.id}>
                            <CardHeader>
                                <div className="flex justify-between">
                                    <CardTitle className="text-lg">{review.title}</CardTitle>
                                    <div className="flex items-center gap-1 text-yellow-500">
                                        <Star className="w-4 h-4 fill-current" />
                                        <span className="font-bold text-foreground">{review.rating}</span>
                                    </div>
                                </div>
                            </CardHeader>
                            <CardContent>
                                <p className="text-muted-foreground">{review.description}</p>
                            </CardContent>
                        </Card>
                    )) : (
                        <p className="text-muted-foreground italic">No reviews yet.</p>
                    )}
                </div>
            </div>
        </div>
    )
}
