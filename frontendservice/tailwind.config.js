/** @type {import('tailwindcss').Config} */
module.exports = {
    content: ["./src/**/*.{vue,ts,jsx,tsx,js}"],

    theme: {
        extend: {
            backgroundImage:{
                'gImage': "url('/src/assets/bg2.jpg')"
            },
            fontFamily: {
                sans: ['"League Spartan"', 'ui-sans-serif', 'system-ui'],
            },
            fontWeight: {
                normal: 400,
                bold: 700,
            },
        },
    },
    plugins: [require('daisyui')],
}