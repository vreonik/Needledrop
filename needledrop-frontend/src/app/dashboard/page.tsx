'use client';

import Link from 'next/link';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';

type AuthUser = {
  username: string;
};

function getAuthUser(): AuthUser | null {
  const token = localStorage.getItem('token');
  const rawUser = localStorage.getItem('user');

  if (!token || !rawUser) return null;

  try {
    return JSON.parse(rawUser) as AuthUser;
  } catch {
    return null;
  }
}

function clearAuth() {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
}

export default function DashboardPage() {
  const router = useRouter();
  const [user, setUser] = useState<AuthUser | null>(null);

  useEffect(() => {
    const authUser = getAuthUser();
    if (!authUser) {
      router.replace('/auth/login');
      return;
    }
    setUser(authUser);
  }, [router]);

  const handleLogout = () => {
    clearAuth();
    router.replace('/');
  };

  if (!user) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-interactive mx-auto" />
          <p className="mt-4 text-neutral-light">Loading...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      <header className="bg-header border-b border-header/30">
        <div className="container mx-auto px-4 py-4">
          <div className="flex justify-between items-center">
            <div className="flex items-center space-x-4">
              <h1 className="text-2xl font-bold">NeedleDrop</h1>
              <nav className="hidden md:flex space-x-6">
                <Link href="/dashboard" className="text-neutral-white hover:text-interactive transition-colors">
                  Dashboard
                </Link>
                <Link href="/albums" className="text-neutral-light hover:text-interactive transition-colors">
                  My Albums
                </Link>
                <Link href="/discover" className="text-neutral-light hover:text-interactive transition-colors">
                  Discover
                </Link>
              </nav>
            </div>

            <div className="flex items-center space-x-4">
              <span className="text-neutral-light">Welcome, {user.username}!</span>
              <button onClick={handleLogout} className="btn-secondary text-sm">
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>

      <main className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <h2 className="text-3xl font-bold mb-2">Your Music Dashboard</h2>
          <p className="text-neutral-light">Welcome to your personal music collection</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="card text-center">
            <h3 className="text-2xl font-bold text-interactive mb-2">0</h3>
            <p className="text-neutral-light">Albums Added</p>
          </div>
          <div className="card text-center">
            <h3 className="text-2xl font-bold text-interactive mb-2">0</h3>
            <p className="text-neutral-light">Reviews Written</p>
          </div>
          <div className="card text-center">
            <h3 className="text-2xl font-bold text-interactive mb-2">0</h3>
            <p className="text-neutral-light">Playlists Created</p>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="card">
            <h3 className="text-xl font-semibold mb-4">Get Started</h3>
            <div className="space-y-3">
              <Link href="/albums/new" className="block btn-primary text-center">
                Add Your First Album
              </Link>
              <Link href="/discover" className="block btn-secondary text-center">
                Discover Music
              </Link>
            </div>
          </div>

          <div className="card">
            <h3 className="text-xl font-semibold mb-4">Recent Activity</h3>
            <p className="text-neutral-light text-center py-8">
              No activity yet. Start exploring when the API wiring is done.
            </p>
          </div>
        </div>
      </main>
    </div>
  );
}
