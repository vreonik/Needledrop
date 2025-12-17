import Link from 'next/link';

export default function DiscoverPage() {
  return (
    <div className="min-h-screen bg-background">
      <header className="bg-header border-b border-header/30">
        <div className="container mx-auto px-4 py-4">
          <div className="flex justify-between items-center">
            <Link href="/" className="text-2xl font-bold text-neutral-white">
              NeedleDrop
            </Link>

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

      <main className="container mx-auto px-4 py-16">
        <div className="text-center max-w-2xl mx-auto">
          <h1 className="text-4xl font-bold mb-6">Discover Music</h1>
          <p className="text-xl text-neutral-light mb-12">
            Browse albums and explore recommendations. This section is currently a UI placeholder.
          </p>

          <div className="card max-w-md mx-auto">
            <h2 className="text-2xl font-semibold mb-4">In Progress</h2>
            <p className="text-neutral-light mb-6">
              Discover features will be connected to the backend API (search, filters, trending).
              For now, this page shows the intended structure.
            </p>
            <Link href="/" className="btn-primary">
              Back to Home
            </Link>
          </div>
        </div>
      </main>
    </div>
  );
}
