'use client';

import Link from 'next/link';
import { useRouter } from 'next/navigation';
import React, { useState } from 'react';

type LoginForm = {
  username: string;
  password: string;
};

const MOCK_TOKEN =
  'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNpY2xvdmVyIiwiaWF0IjoxNzYzNzMyMDQyLCJleHAiOjE3NjM3NjgwNDJ9.kELlZw1SerBK19rze8rju5RV35du5JZDGn4DgimQXiM';

export default function LoginPage() {
  const router = useRouter();

  const [form, setForm] = useState<LoginForm>({ username: '', password: '' });
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);

  const updateField = (field: keyof LoginForm) => (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm((prev) => ({ ...prev, [field]: e.target.value }));
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      // Temporary: mock login until backend integration is wired up.
      await new Promise((resolve) => setTimeout(resolve, 800));

      localStorage.setItem('token', MOCK_TOKEN);
      localStorage.setItem('user', JSON.stringify({ username: form.username }));

      router.replace('/dashboard');
    } catch {
      setError('Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-background">
      <div className="card max-w-md w-full">
        <h1 className="text-3xl font-bold text-center mb-8">Sign In</h1>

        {error ? (
          <div className="bg-red-500/20 border border-red-500 text-red-300 px-4 py-3 rounded mb-4">
            {error}
          </div>
        ) : null}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label htmlFor="username" className="block text-sm font-medium mb-2">
              Username
            </label>
            <input
              id="username"
              type="text"
              value={form.username}
              onChange={updateField('username')}
              autoComplete="username"
              className="w-full px-3 py-2 bg-header border border-header rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-interactive"
              placeholder="Enter your username"
              required
              disabled={loading}
            />
          </div>

          <div>
            <label htmlFor="password" className="block text-sm font-medium mb-2">
              Password
            </label>
            <input
              id="password"
              type="password"
              value={form.password}
              onChange={updateField('password')}
              autoComplete="current-password"
              className="w-full px-3 py-2 bg-header border border-header rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-interactive"
              placeholder="Enter your password"
              required
              disabled={loading}
            />
          </div>

          <button type="submit" disabled={loading} className="w-full btn-primary disabled:opacity-50">
            {loading ? 'Signing In...' : 'Sign In'}
          </button>
        </form>

        <p className="text-center mt-6 text-neutral-light">
          Don&apos;t have an account?{' '}
          <Link href="/auth/register" className="text-interactive hover:underline">
            Sign up
          </Link>
        </p>
      </div>
    </div>
  );
}
