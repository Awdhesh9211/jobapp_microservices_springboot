import { useEffect, useState } from "react";
import { getAllJobs, createJob } from "@/api/jobs";
import { getAllCompanies } from "@/api/companies";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle, CardDescription, CardFooter } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Skeleton } from "@/components/ui/skeleton";
import { Plus, MapPin, DollarSign, Building } from "lucide-react";
import { Link } from "react-router-dom";

export default function JobsPage() {
    const [jobs, setJobs] = useState([]);
    const [companies, setCompanies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [open, setOpen] = useState(false);

    // Form state
    const [formData, setFormData] = useState({
        title: "",
        description: "",
        minSalary: "",
        maxSalary: "",
        location: "",
        comapnyId: ""
    });

    useEffect(() => {
        fetchJobs();
        fetchCompanies();
    }, []);

    const fetchJobs = async () => {
        try {
            const res = await getAllJobs();
            setJobs(res.data);
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const fetchCompanies = async () => {
        try {
            const res = await getAllCompanies();
            setCompanies(res.data);
        } catch (error) {
            console.error(error);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await createJob(formData);
            setOpen(false);
            setFormData({
                title: "",
                description: "",
                minSalary: "",
                maxSalary: "",
                location: "",
                comapnyId: ""
            });
            fetchJobs();
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div className="space-y-8">
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-3xl font-bold tracking-tight">Jobs</h1>
                    <p className="text-muted-foreground mt-2">Find your next role from our curated list.</p>
                </div>
                <Dialog open={open} onOpenChange={setOpen}>
                    <DialogTrigger asChild>
                        <Button className="gap-2"><Plus className="w-4 h-4" /> Post a Job</Button>
                    </DialogTrigger>
                    <DialogContent className="max-w-[600px]">
                        <DialogHeader>
                            <DialogTitle>Post New Job</DialogTitle>
                        </DialogHeader>
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div className="grid grid-cols-2 gap-4">
                                <div className="space-y-2">
                                    <Label htmlFor="title">Job Title</Label>
                                    <Input
                                        id="title"
                                        value={formData.title}
                                        onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                                        required
                                    />
                                </div>
                                <div className="space-y-2">
                                    <Label htmlFor="company">Company</Label>
                                    <Select
                                        value={formData.comapnyId}
                                        onValueChange={(val) => setFormData({ ...formData, comapnyId: val })}
                                    >
                                        <SelectTrigger>
                                            <SelectValue placeholder="Select Company" />
                                        </SelectTrigger>
                                        <SelectContent>
                                            {companies.map(c => (
                                                <SelectItem key={c.id} value={String(c.id)}>{c.name}</SelectItem>
                                            ))}
                                        </SelectContent>
                                    </Select>
                                </div>
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="description">Description</Label>
                                <Textarea
                                    id="description"
                                    value={formData.description}
                                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                                    required
                                />
                            </div>

                            <div className="grid grid-cols-2 gap-4">
                                <div className="space-y-2">
                                    <Label htmlFor="minSalary">Min Salary</Label>
                                    <Input
                                        id="minSalary"
                                        value={formData.minSalary}
                                        onChange={(e) => setFormData({ ...formData, minSalary: e.target.value })}
                                        placeholder="e.g. 50k"
                                        required
                                    />
                                </div>
                                <div className="space-y-2">
                                    <Label htmlFor="maxSalary">Max Salary</Label>
                                    <Input
                                        id="maxSalary"
                                        value={formData.maxSalary}
                                        onChange={(e) => setFormData({ ...formData, maxSalary: e.target.value })}
                                        placeholder="e.g. 80k"
                                        required
                                    />
                                </div>
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="location">Location</Label>
                                <Input
                                    id="location"
                                    value={formData.location}
                                    onChange={(e) => setFormData({ ...formData, location: e.target.value })}
                                    placeholder="e.g. Remote, New York"
                                    required
                                />
                            </div>

                            <Button type="submit" className="w-full">Create Job</Button>
                        </form>
                    </DialogContent>
                </Dialog>
            </div>

            {loading ? (
                <div className="space-y-4">
                    {[1, 2, 3].map(i => <Skeleton key={i} className="h-[150px] w-full rounded-xl" />)}
                </div>
            ) : (
                <div className="grid gap-4">
                    {jobs.map(dto => (
                        <Card key={dto.job.id} className="hover:border-primary/50 transition-colors">
                            <CardHeader>
                                <div className="flex justify-between items-start">
                                    <div>
                                        <CardTitle className="text-xl mb-1">{dto.job.title}</CardTitle>
                                        <div className="flex items-center text-muted-foreground text-sm gap-4">
                                            <span className="flex items-center gap-1"><Building className="w-4 h-4" /> {dto.company?.name || "Unknown Company"}</span>
                                            <span className="flex items-center gap-1"><MapPin className="w-4 h-4" /> {dto.job.location}</span>
                                        </div>
                                    </div>
                                    <Button asChild variant="secondary">
                                        <Link to={`/jobs/${dto.job.id}`}>View Details</Link>
                                    </Button>
                                </div>
                            </CardHeader>
                            <CardContent>
                                <CardDescription className="line-clamp-2">
                                    {dto.job.description}
                                </CardDescription>
                            </CardContent>
                            <CardFooter className="text-sm text-muted-foreground border-t pt-4">
                                <span className="flex items-center gap-1 text-green-600 font-medium">
                                    <DollarSign className="w-4 h-4" />
                                    {dto.job.minSalary} - {dto.job.maxSalary}
                                </span>
                            </CardFooter>
                        </Card>
                    ))}
                </div>
            )}
        </div>
    )
}
