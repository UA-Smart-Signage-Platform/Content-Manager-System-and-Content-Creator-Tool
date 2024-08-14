import { create } from "zustand";
import {getCookie,setCookie,removeCookie} from "typescript-cookie"
import { createJSONStorage, persist } from "zustand/middleware";

const TOKEN_NAME = "jwtToken";

export const useUserStore = create(
    persist(
        (set,get) => ({
            logged: false,
            username: "",
            role: null,
            token: () => {
                return getCookie(TOKEN_NAME)
            },

            login: (username,role,token) => {
                set({logged:true,username:username,role:role});
                setCookie(TOKEN_NAME,token,{expires:3,secure:true})
            },

            logout: () => {
                set({logged:false,username:"",role:null});
                removeCookie(TOKEN_NAME)
            }
        }),
        {
            name:"user-storage",
            storage: createJSONStorage(() => localStorage),
        }
    )
)