import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";

export default function Navbar() {
    return (
        <nav className="border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 sticky top-0 z-50">
            <div className="container flex h-16 items-center px-4">
                <Link to="/" className="mr-8 flex items-center space-x-2">
                    <div className="h-6 w-6 bg-primary rounded-full" />
                    <span className="font-bold text-xl tracking-tight">JobNexus</span>
                </Link>
                <div className="mr-4 flex flex-1 items-center justify-between">
                    <nav className="flex items-center space-x-6 text-sm font-medium">
                        <Link to="/companies" className="transition-colors hover:text-primary text-muted-foreground">Companies</Link>
                        <Link to="/job" className="transition-colors hover:text-primary text-muted-foreground">Jobs</Link>
                    </nav>
                    <div className="flex items-center space-x-4">
                        <Button variant="ghost" size="sm">Log In</Button>
                        <Button size="sm">Get Started</Button>
                    </div>
                </div>
            </div>
        </nav>
    )
}
