import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";

export const useThemeStore = create(
  persist(
    (set, get) => ({
      theme: "dark",

      changeTheme: (theme) =>{
        set(()=>({
          theme: theme
        }));
      },
    }),
    {
      name: "theme-storage", 
      storage: createJSONStorage(() => localStorage),
    }
  )
);
