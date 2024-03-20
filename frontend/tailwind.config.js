/** @type {import('tailwindcss').Config} */

const { createThemes } = require('tw-colors');
const colors = require('tailwindcss/colors');

module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      fontFamily: { 
        "Lexend": ['Lexend', 'sans-serif'] 
      }  
    },
  },
  plugins: [
    createThemes({
      ligth:{
        'primary':'#96d600',
        'accent':'#57e574',
        'secondary':'#a7a8a9',
        'secondaryLigth':"#E9E9E9",
        'red':"#D12E2E",
        'textcolor':'#101604',
        'background':'#fafdf7',
      },
      dark:{
        'primary':'#bfff29',
        'accent':'#1aa836',
        'secondary':'#5B5C5C',
        'secondaryLigth':"#3D3D3E",
        'red':'#9D2929',
        'textcolor':'#f5fbe9',
        'background':'#1C1D1F',
      }
    })
  ],
}

