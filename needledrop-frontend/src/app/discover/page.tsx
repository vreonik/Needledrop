import Link from 'next/link'

export default function DiscoverPage() {
  return (
    <div className="min-h-screen bg-background">
      {/* Navigation Header */}
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

      {/* Main Content */}
      <main className="container mx-auto px-4 py-16">
        <div className="text-center max-w-2xl mx-auto">
          <h1 className="text-4xl font-bold mb-6">Discover Music</h1>
          <p className="text-xl text-neutral-light mb-12">
            Explore trending albums and everything you wish because I said it like that.
          </p>

          {/* Coming Soon Placeholder */}
          <div className="card max-w-md mx-auto">
            <h2 className="text-2xl font-semibold mb-4">Coming Soon</h2>
            <p className="text-neutral-light mb-6">
              Music features are currently in development, check in a few months or never:)
            </p>
            <Link href="/" className="btn-primary">
              Back to Home
            </Link>
          </div>
        </div>
      </main>
    </div>
  )
}