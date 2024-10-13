export const weekDays = [
    { value: 0, label: "MON" },
    { value: 1, label: "TUE" },
    { value: 2, label: "WED" },
    { value: 3, label: "THU" },
    { value: 4, label: "FRI" },
    { value: 5, label: "SAT" },
    { value: 6, label: "SUN" }
];

export const getWeekDaysValues = () => weekDays.map(day => day.value);

export const timeHour = Array.from({ length: 24 }, (_, i) => i.toString().padStart(2, "0"));
export const timeMinute = Array.from({ length: 12 }, (_, i) => (i * 5).toString().padStart(2, "0"));

export const colors = [
    'bg-pink-200 border-pink-400',
    'bg-rose-200 border-rose-400',
    'bg-violet-200 border-violet-400',
    'bg-blue-200 border-blue-400',
    'bg-green-200 border-green-400',
    'bg-yellow-200 border-yellow-400',
    'bg-stone-200 border-stone-400',
    'bg-red-200 border-red-400',
    'bg-lime-200 border-lime-400',
    'bg-teal-200 border-teal-400',
    'bg-cyan-200 border-cyan-400',
    'bg-amber-200 border-amber-400',
    'bg-emerald-200 border-emerald-400',
    'bg-indigo-200 border-indigo-400',
    'bg-fuchsia-200 border-fuchsia-400',
    'bg-sky-200 border-sky-400',
    'bg-purple-200 border-purple-400',
    'bg-gray-200 border-gray-400',
    'bg-zinc-200 border-zinc-400',
    'bg-slate-200 border-slate-400',
    'bg-neutral-200 border-neutral-400'
];