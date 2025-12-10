'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';

export default function LoginPage() {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const router = useRouter();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            // For now, we'll simulate a successful login
            // In the next step, we'll connect to the actual backend
            console.log('Login attempt:', formData);

            // Simulate API call delay
            await new Promise(resolve => setTimeout(resolve, 1000));

            // Store a mock token (replace this with real auth later)
            localStorage.setItem('token', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdXNpY2xvdmVyIiwiaWF0IjoxNzYzNzMyMDQyLCJleHAiOjE3NjM3NjgwNDJ9.kELlZw1SerBK19rze8rju5RV35du5JZDGn4DgimQXiM');
            localStorage.setItem('user', JSON.stringify({ username: formData.username }));

            router.push('/dashboard');
        } catch (err: any) {
            setError(err.response?.data?.message || 'Login failed');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-background">
            <div className="card max-w-md w-full">
                <h1 className="text-3xl font-bold text-center mb-8">Sign In</h1>

                {error && (
                    <div className="bg-red-500/20 border border-red-500 text-red-300 px-4 py-3 rounded mb-4">
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label htmlFor="username" className="block text-sm font-medium mb-2">
                            Username
                        </label>
                        <input
                            type="text"
                            id="username"
                            value={formData.username}
                            onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                            className="w-full px-3 py-2 bg-header border border-header rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-interactive"
                            placeholder="Enter your username"
                            required
                        />
                    </div>

                    <div>
                        <label htmlFor="password" className="block text-sm font-medium mb-2">
                            Password
                        </label>
                        <input
                            type="password"
                            id="password"
                            value={formData.password}
                            onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                            className="w-full px-3 py-2 bg-header border border-header rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-interactive"
                            placeholder="Enter your password"
                            required
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full btn-primary disabled:opacity-50"
                    >
                        {loading ? 'Signing In...' : 'Sign In'}
                    </button>
                </form>

                <p className="text-center mt-6 text-neutral-light">
                    Don't have an account?{' '}
                    <Link href="/auth/register" className="text-interactive hover:underline">
                        Sign up
                    </Link>
                </p>
            </div>
        </div>
    );
}