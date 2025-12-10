import Link from 'next/link'

export default function Home() {
    return (
        <main className="min-h-screen">
            {/* Navigation Header */}
            <header className="absolute top-0 left-0 right-0 z-10">
                <div className="container mx-auto px-4 py-6">
                    <div className="flex justify-between items-center">
                        {/* Logo - Optional, can remove if you want only the big title */}
                        <div className="text-xl font-bold text-neutral-white">
                            NeedleDrop
                        </div>

                        {/* Auth Links */}
                        <div className="space-x-4">
                            <Link href="/auth/login" className="text-neutral-light hover:text-interactive transition-colors">
                                Sign In
                            </Link>
                            <Link href="/auth/register" className="btn-primary">
                                Register
                            </Link>
                        </div>
                    </div>
                </div>
            </header>

            {/* Hero Section - Centered */}
            <section className="min-h-screen flex items-center justify-center bg-gradient-primary text-neutral-white relative">
                <div className="container mx-auto px-4 text-center">
                    {/* Big Centered Title */}
                    <h1 className="text-7xl md:text-8xl font-bold mb-8 tracking-tight">
                        NeedleDrop
                    </h1>

                    {/* Subtitle */}
                    <p className="text-xl md:text-2xl mb-12 opacity-90 max-w-2xl mx-auto">
                        Your personal music box|_| Discover, organize, and share your music taste.
                    </p>

                    {/* Main CTA Button */}
                    <div className="space-x-4">
                        <Link
                            href="/discover"
                            className="btn-secondary text-lg px-8 py-4 text-xl font-semibold"
                        >
                            Explore Music
                        </Link>
                    </div>
                </div>

                {/* Optional: Scroll indicator */}
                <div className="absolute bottom-8 left-1/2 transform -translate-x-1/2 animate-bounce">
                    <div className="w-6 h-10 border-2 border-neutral-white rounded-full flex justify-center">
                        <div className="w-1 h-3 bg-neutral-white rounded-full mt-2"></div>
                    </div>
                </div>
            </section>

            {/* Features Section - Below the fold */}
            <section className="py-20 bg-background">
                <div className="container mx-auto px-4">
                    <h2 className="text-3xl font-bold text-center mb-16">NeedleDrop is so good!</h2>
                    <div className="grid md:grid-cols-3 gap-8 max-w-4xl mx-auto">
                        <div className="card text-center">
                            <h3 className="text-xl font-semibold mb-4">Music Organization</h3>
                            <p className="text-neutral-light">Create and manage your personal music collection</p>
                        </div>

                        <div className="card text-center">
                            <h3 className="text-xl font-semibold mb-4">Community Reviews</h3>
                            <p className="text-neutral-light">Share your thoughts and discover new music</p>
                        </div>

                        <div className="card text-center">
                            <h3 className="text-xl font-semibold mb-4">Very good design</h3>
                            <p className="text-neutral-light">I tried.</p>
                        </div>
                    </div>
                </div>
            </section>
        </main>
    )
}