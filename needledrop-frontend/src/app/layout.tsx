import type { Metadata } from 'next'
import './globals.css'

export const metadata: Metadata = {
    title: 'NeedleDrop - Your Music Letter Box',
    description: 'Discover, organize, and share your music collection',
}

export default function RootLayout({
                                       children,
                                   }: {
    children: React.ReactNode
}) {
    return (
        <html lang="en">
        <body className="min-h-screen bg-background text-neutral-white">
        {children}
        </body>
        </html>
    )
}