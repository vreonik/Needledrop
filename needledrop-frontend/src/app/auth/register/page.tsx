'use client';

import Link from 'next/link';
import { useRouter } from 'next/navigation';
import React, { useMemo, useState } from 'react';

type RegisterForm = {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
};

type RegisterErrors = Record<keyof RegisterForm | 'general', string>;

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? 'http://localhost:8080';

function validateUsername(value: string): string {
  if (value.length < 3) return 'Username must be at least 3 characters';
  if (value.length > 20) return 'Username must be less than 20 characters';
  if (!/^[a-zA-Z0-9_]+$/.test(value)) return 'Username can only contain letters, numbers, and underscores';
  return '';
}

function validateEmail(value: string): string {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(value)) return 'Please enter a valid email address';
  return '';
}

function validatePassword(value: string): string {
  if (value.length < 6) return 'Password must be at least 6 characters';
  return '';
}

export default function RegisterPage() {
  const router = useRouter();

  const [form, setForm] = useState<RegisterForm>({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
  });

  const [errors, setErrors] = useState<RegisterErrors>({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    general: '',
  });

  const [loading, setLoading] = useState(false);

  const validateField = (name: keyof RegisterForm, value: string): string => {
    switch (name) {
      case 'username':
        return validateUsername(value);
      case 'email':
        return validateEmail(value);
      case 'password':
        return validatePassword(value);
      case 'confirmPassword':
        return value !== form.password ? 'Passwords do not match' : '';
      default:
        return '';
    }
  };

  const updateField =
    (name: keyof RegisterForm) => (e: React.ChangeEvent<HTMLInputElement>) => {
      const value = e.target.value;

      setForm((prev) => ({ ...prev, [name]: value }));

      // Real-time validation
      setErrors((prev) => ({
        ...prev,
        [name]: validateField(name, value),
        general: prev.general ? '' : prev.general,
      }));
    };

  const isFormValid = useMemo(() => {
    const hasValues =
      form.username && form.email && form.password && form.confirmPassword;

    const hasNoErrors =
      !errors.username && !errors.email && !errors.password && !errors.confirmPassword;

    return Boolean(hasValues && hasNoErrors);
  }, [form, errors]);

  const validateAll = (): RegisterErrors => {
    return {
      username: validateField('username', form.username),
      email: validateField('email', form.email),
      password: validateField('password', form.password),
      confirmPassword: validateField('confirmPassword', form.confirmPassword),
      general: '',
    };
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    setErrors((prev) => ({ ...prev, general: '' }));

    const nextErrors = validateAll();
    setErrors(nextErrors);

    const hasAnyErrors = Object.values(nextErrors).some((msg) => msg.length > 0);
    if (hasAnyErrors) {
      setLoading(false);
      return;
    }

    try {
      const res = await fetch(`${API_URL}/api/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          username: form.username,
          email: form.email,
          password: form.password,
        }),
      });

      const data = await res.json().catch(() => null);

      if (!res.ok) {
        const message = data?.message ?? 'Registration failed';
        throw new Error(message);
      }

      // Expected: { token, user } from backend
      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(data.user));

      router.replace('/dashboard');
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Registration failed. Please try again.';
      setErrors((prev) => ({ ...prev, general: message }));
    } finally {
      setLoading(false);
    }
  };

  const inputBase =
    'w-full px-3 py-2 bg-header border rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-interactive';

  const inputBorder = (hasError: boolean) => (hasError ? 'border-red-500' : 'border-header');

  return (
    <div className="min-h-screen flex items-center justify-center bg-background">
      <div className="card max-w-md w-full">
        <h1 className="text-3xl font-bold text-center mb-8">Create Account</h1>

        {errors.general ? (
          <div className="bg-red-500/20 border border-red-500 text-red-300 px-4 py-3 rounded mb-4">
            {errors.general}
          </div>
        ) : null}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label htmlFor="username" className="block text-sm font-medium mb-2">
              Username
            </label>
            <input
              id="username"
              name="username"
              type="text"
              value={form.username}
              onChange={updateField('username')}
              autoComplete="username"
              placeholder="Choose a username (3-20 characters)"
              className={`${inputBase} ${inputBorder(Boolean(errors.username))}`}
              required
              disabled={loading}
            />
            {errors.username ? <p className="text-red-400 text-sm mt-1">{errors.username}</p> : null}
          </div>

          <div>
            <label htmlFor="email" className="block text-sm font-medium mb-2">
              Email
            </label>
            <input
              id="email"
              name="email"
              type="email"
              value={form.email}
              onChange={updateField('email')}
              autoComplete="email"
              placeholder="Enter your email"
              className={`${inputBase} ${inputBorder(Boolean(errors.email))}`}
              required
              disabled={loading}
            />
            {errors.email ? <p className="text-red-400 text-sm mt-1">{errors.email}</p> : null}
          </div>

          <div>
            <label htmlFor="password" className="block text-sm font-medium mb-2">
              Password
            </label>
            <input
              id="password"
              name="password"
              type="password"
              value={form.password}
              onChange={updateField('password')}
              autoComplete="new-password"
              placeholder="Enter your password: "
              className={`${inputBase} ${inputBorder(Boolean(errors.password))}`}
              required
              disabled={loading}
            />
            {errors.password ? <p className="text-red-400 text-sm mt-1">{errors.password}</p> : null}
          </div>

          <div>
            <label htmlFor="confirmPassword" className="block text-sm font-medium mb-2">
              Confirm Password
            </label>
            <input
              id="confirmPassword"
              name="confirmPassword"
              type="password"
              value={form.confirmPassword}
              onChange={updateField('confirmPassword')}
              autoComplete="new-password"
              placeholder="Confirm your password"
              className={`${inputBase} ${inputBorder(Boolean(errors.confirmPassword))}`}
              required
              disabled={loading}
            />
            {errors.confirmPassword ? (
              <p className="text-red-400 text-sm mt-1">{errors.confirmPassword}</p>
            ) : null}
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
