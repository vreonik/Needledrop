/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
        './src/components/**/*.{js,ts,jsx,tsx,mdx}',
        './src/app/**/*.{js,ts,jsx,tsx,mdx}',
    ],
    theme: {
        extend: {
            colors: {
                'header': '#4B4A7F',
                'background': '#2F2E4F',
                'buttons': '#C6B8FF',
                'interactive': '#5BA4FF',
                'neutral-light': '#E9E9F2',
                'neutral-white': '#FCFCFF',
                'text-dark': '#0F0F1A',
            },
            backgroundImage: {
                'gradient-primary': 'linear-gradient(90deg, #4B4A7F 0%, #C6B8FF 50%, #C288D1 100%)',
                'gradient-card': 'linear-gradient(135deg, #4B4A7F 0%, #2F2E4F 100%)',
            }
        },
    },
    plugins: [
        require('@tailwindcss/forms'),
        require('@tailwindcss/typography'),
    ],
}