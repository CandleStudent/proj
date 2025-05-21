module.exports = {
  content: [
    './index.html',
    './src/**/*.{vue,js,ts,jsx,tsx}',
  ],
  safelist: [
    'rounded',
    'rounded-md',
    'rounded-lg',
    'bg-green-500',
    'p-2',
    // Добавь нужные тебе классы
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}