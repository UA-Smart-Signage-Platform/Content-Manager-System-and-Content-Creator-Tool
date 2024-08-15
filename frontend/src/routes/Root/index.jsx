import { NavBar } from "../../components";
import { Outlet } from "react-router";
import { useThemeStore } from "../../stores/useThemeStore";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import loginService from "../../services/loginService";
import { createTheme } from "react-data-table-component";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useUserStore } from "../../stores/useUserStore";

const CLIENT_ID = import.meta.env.REACT_APP_WSO2_CLIENT_ID;
const REDIRECT_URI = import.meta.env.REACT_APP_WSO2_REDIRECT_URI;
const IDP_URI = import.meta.env.REACT_APP_IDP_URI;
const BASE64 = import.meta.env.REACT_APP_IDP_BASE64;

createTheme('solarized', {
    text: {
      primary: '#101604',
    },
    background: {
      default: '#fafdf7',
    },
    divider: {
      default: '#073642',
    },
});

const queryClient = new QueryClient();

function Root(){
    const theme = useThemeStore((state) =>state.theme)
    const logged = useUserStore((state) => state.logged)

    const location = useLocation();

    return(
        <div className={`h-screen flex ${theme} text-textcolor bg-backgroundcolor`}>
            <div id="body" className="h-[100vh] w-full">
                <div id="page-content" className="ml-[65px] p-4 pr-20 h-full">
                  {(logged || location.pathname == "/login") &&
                    <QueryClientProvider client={queryClient} contextSharing={true}>
                      <Outlet/>
                    </QueryClientProvider>
                  }
                </div>
            </div>
            <NavBar/>
        </div>
    )
}

export default Root;