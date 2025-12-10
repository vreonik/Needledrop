'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';

export default function RegisterPage() {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
    });
    const [errors, setErrors] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        general: '', // Add general error to initial state
    });
    const [loading, setLoading] = useState(false);

    const router = useRouter();

    const validateField = (name: string, value: string) => {
        switch (name) {
            case 'username':
                if (value.length < 3) return 'Username must be at least 3 characters';
                if (value.length > 20) return 'Username must be less than 20 characters';
                if (!/^[a-zA-Z0-9_]+$/.test(value)) return 'Username can only contain letters, numbers, and underscores';
                return '';

            case 'email':
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailRegex.test(value)) return 'Please enter a valid email address';
                return '';

            case 'password':
                if (value.length < 6) return 'Password must be at least 6 characters';
                return '';

            case 'confirmPassword':
                if (value !== formData.password) return 'Passwords do not match';
                return '';

            default:
                return '';
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));

        // Real-time validation
        const error = validateField(name, value);
        setErrors(prev => ({ ...prev, [name]: error }));

        // Clear general error when user starts typing
        if (errors.general) {
            setErrors(prev => ({ ...prev, general: '' }));
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setErrors(prev => ({ ...prev, general: '' })); // Clear previous general errors

        // Validate all fields
        const newErrors = {
            username: validateField('username', formData.username),
            email: validateField('email', formData.email),
            password: validateField('password', formData.password),
            confirmPassword: validateField('confirmPassword', formData.confirmPassword),
            general: '', // Include general in validation object
        };

        setErrors(newErrors);

        // Check if there are any field errors
        const hasFieldErrors = Object.values(newErrors).some(error => error !== '');
        if (hasFieldErrors) {
            setLoading(false);
            return;
        }

        try {
            // Replace this with your actual API call
            const response = await fetch('http://localhost:8080/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: formData.username,
                    email: formData.email,
                    password: formData.password,
                }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Registration failed');
            }

            const data = await response.json();

            // Store the token and user data
            localStorage.setItem('token', data.token);
            localStorage.setItem('user', JSON.stringify(data.user));

            router.push('/dashboard');
        } catch (err: any) {
            setErrors(prev => ({
                ...prev,
                general: err.message || 'Registration failed. Please try again.'
            }));
        } finally {
            setLoading(false);
        }
    };

    const isFormValid = formData.username &&
        formData.email &&
        formData.password &&
        formData.confirmPassword &&
        !errors.username &&
        !errors.email &&
        !errors.password &&
        !errors.confirmPassword;

    return (
        <div className="min-h-screen flex items-center justify-center bg-background">
            <div className="card max-w-md w-full">
                <h1 className="text-3xl font-bold text-center mb-8">Create Account</h1>

                {errors.general && (
                    <div className="bg-red-500/20 border border-red-500 text-red-300 px-4 py-3 rounded mb-4">
                        {errors.general}
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
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                            className={`w-full px-3 py-2 bg-header border rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-interactive ${
                                errors.username ? 'border-red-500' : 'border-header'
                            }`}
                            placeholder="Choose a username (3-20 characters)"
                            required
                        />
                        {errors.username && <p className="text-red-400 text-sm mt-1">{errors.username}</p>}
                    </div>

                    <div>
                        <label htmlFor="email" className="block text-sm font-medium mb-2">
                            Email
                        </label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            className={`w-full px-3 py-2 bg-header border rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-interactive ${
                                errors.email ? 'border-red-500' : 'border-header'
                            }`}
                            placeholder="Enter your email"
                            required
                        />
                        {errors.email && <p className="text-red-400 text-sm mt-1">{errors.email}</p>}
                    </div>

                    <div>
                        <label htmlFor="password" className="block text-sm font-medium mb-2">
                            Password
                        </label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            className={`w-full px-3 py-2 bg-header border rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-interactive ${
                                errors.password ? 'border-red-500' : 'border-header'
                            }`}
                            placeholder="Enter your password (min. 6 characters)"
                            required
                        />
                        {errors.password && <p className="text-red-400 text-sm mt-1">{errors.password}</p>}
                    </div>

                    <div>
                        <label htmlFor="confirmPassword" className="block text-sm font-medium mb-2">
                            Confirm Password
                        </label>
                        <input
                            type="password"
                            id="confirmPassword"
                            name="confirmPassword"
                            value={formData.confirmPassword}
                            onChange={handleChange}
                            className={`w-full px-3 py-2 bg-header border rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-interactive ${
                                errors.confirmPassword ? 'border-red-500' : 'border-header'
                            }`}
                            placeholder="Confirm your password"
                            required
                        />
                        {errors.confirmPassword && <p className="text-red-400 text-sm mt-1">{errors.confirmPassword}</p>}
                    </div>

                    <button
                        type="submit"
                        disabled={loading || !isFormValid}
                        className="w-full btn-primary disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                        {loading ? 'Creating Account...' : 'Create Account'}
                    </button>
                </form>

                <p className="text-center mt-6 text-neutral-light">
                    Already have an account?{' '}
                    <Link href="/auth/login" className="text-interactive hover:underline">
                        Sign in
                    </Link>
                </p>
            </div>
        </div>
    );
}