import { Button } from "@/components/ui/button";
import { Link } from "react-router-dom";
import { ArrowRight, Building2, Briefcase } from "lucide-react";

export default function HomePage() {
    return (
        <div className="flex flex-col items-center justify-center py-20 text-center space-y-8">
            <div className="space-y-4 max-w-3xl">
                <h1 className="text-4xl font-extrabold tracking-tight lg:text-6xl bg-gradient-to-r from-primary to-blue-600 bg-clip-text text-transparent">
                    Find Your Dream Career <br /> in the Microservices Era
                </h1>
                <p className="text-xl text-muted-foreground mx-auto max-w-[700px]">
                    Connect with top companies and discover opportunities that match your skills.
                    Built with efficiency and scalability in mind.
                </p>
            </div>

            <div className="flex gap-4">
                <Link to="/job">
                    <Button size="lg" className="h-12 px-8 text-lg gap-2">
                        Browse Jobs <Briefcase className="w-5 h-5" />
                    </Button>
                </Link>
                <Link to="/companies">
                    <Button size="lg" variant="outline" className="h-12 px-8 text-lg gap-2">
                        Explore Companies <Building2 className="w-5 h-5" />
                    </Button>
                </Link>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mt-16 w-full max-w-5xl text-left">
                <FeatureCard
                    icon={<Briefcase className="w-10 h-10 text-primary mb-4" />}
                    title="Thousands of Jobs"
                    desc="Browse through a vast listing of opportunities from top tech firms."
                />
                <FeatureCard
                    icon={<Building2 className="w-10 h-10 text-primary mb-4" />}
                    title="Top Companies"
                    desc="Get insights into company culture, reviews, and open positions."
                />
                <FeatureCard
                    icon={<ArrowRight className="w-10 h-10 text-primary mb-4" />}
                    title="Easy Application"
                    desc="Apply to your dream job with just a few clicks and track status."
                />
            </div>
        </div>
    )
}

function FeatureCard({ icon, title, desc }) {
    return (
        <div className="p-6 rounded-xl border bg-card text-card-foreground shadow-sm hover:shadow-md transition-shadow">
            {icon}
            <h3 className="font-semibold text-xl mb-2">{title}</h3>
            <p className="text-muted-foreground">{desc}</p>
        </div>
    )
}
