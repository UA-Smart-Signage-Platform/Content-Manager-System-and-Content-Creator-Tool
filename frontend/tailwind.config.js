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
      light:{
        'primary':'#96d600',
        'accent':'#57e574',
        'secondary':'#a7a8a9',
        'secondaryMedium':'#d7dad6',
        'secondaryLight':"#E9E9E9",
        'red':"#D12E2E",
        'orange': "#DC8331",
        'grey': "#A1A1A1",
        'textcolor':'#101604',
        'backgroundcolor':'#fafdf7',
        'searchButton':'#AFAFAF',
        'selectedGroup':'#C7C7C7',
        'textcolorNotSelected':'#4D5146',
      },
      dark:{
        'primary':'#bfff29',
        'accent':'#1aa836',
        'secondary':'#5B5C5C',
        'secondaryMedium':'#272826',
        'secondaryLight':"#3D3D3E",
        'red':'#9D2929',
        'textcolor':'#f5fbe9',
        'backgroundcolor':'#1C1D1F',
        // TODO - to be changed
        'searchButton':'#AFAFAF', 
        'selectedGroup':'#C7C7C7',
        'textcolorNotSelected':'#4D5146',
      }
    })
  ],
}

