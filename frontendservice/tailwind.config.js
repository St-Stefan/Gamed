/** @type {import('tailwindcss').Config} */
module.exports = {
    content: ["./src/**/*.{vue,ts,jsx,tsx,js}"],

    theme: {
        extend: {},
    },
    plugins: [require('daisyui')],
}