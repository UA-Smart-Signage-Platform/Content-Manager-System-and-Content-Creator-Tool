import { NavBar } from "../../components";
import { Outlet } from "react-router";
import { useThemeStore } from "../../stores/useThemeStore";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";


const CLIENT_ID = process.env.REACT_APP_WSO2_CLIENT_ID;
const REDIRECT_URI = process.env.REACT_APP_WSO2_REDIRECT_URI;
const IDP_URI = process.env.REACT_APP_IDP_URI;
const BASE64 = process.env.REACT_APP_IDP_BASE64;


function Root(){
    const theme = useThemeStore((state) =>state.theme)

    const location = useLocation();
    const navigate = useNavigate();
    const [currentPath, setCurrentPath] = useState(location.pathname);

    const redirectToLogin = () => {
        window.location.replace(`${IDP_URI}/authorize?response_type=code&client_id=${CLIENT_ID}&state=1234567890&scope=openid&redirect_uri=${REDIRECT_URI}`);
      };

    const getAccessToken = (code) => {
        fetch(`${IDP_URI}/token?code=${code}&redirect_uri=${REDIRECT_URI}&grant_type=authorization_code`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                Authorization: `${BASE64}`
            }
        })
            .then((response) => response.json())
            .then(async (data) => {
            console.log("data", data);
            let seconds = data.expires_in;
            
            await Promise.all([
              setWithExpiry('access_token', data.access_token, seconds * 1000),
              setWithExpiry('id_token', data.id_token, seconds * 1000),
              localStorage.setItem('refresh_token', data.refresh_token)
            ]);
            
            setTimeout(() => {
              navigate("/dashboard");
          }, 1000);
          
          });
      };
      
      const setWithExpiry = (key, value, ttl) => {
        const now = new Date();
        const item = {
          value: value,
          expiry: now.getTime() + ttl,
        };
        localStorage.setItem(key, JSON.stringify(item));
      };
      
      const getWithExpiry = (key) => {
        const itemStr = localStorage.getItem(key);
        if (!itemStr) {
          return "";
        }
        const item = JSON.parse(itemStr);
        const now = new Date();
        if (now.getTime() > item.expiry) {
          localStorage.removeItem(key);
          redirectToLogin();
          return "";
        } else if (key === "id_token" && item.expiry - now.getTime() < 600 * 1000) {
          console.log("TODO");
          // getRefreshToken();
          redirectToLogin();
        }
        return item.value;
      };

    useEffect(() => {
        setCurrentPath(location.pathname);
    
        if (currentPath != 'login') {
          const params = new URLSearchParams(location.search);
          const params_code = params.get('code');
          
          console.log(getWithExpiry("access_token"));

    
          if (!params_code && !getWithExpiry("access_token")) {
            if (currentPath !== '/login') {
              window.location.href = "/login";
            }
          } else if (params_code && !getWithExpiry("access_token")) {
            getAccessToken(params_code);
          }
        }
      }, [currentPath, location.search]);

    return(
        <div className={`h-screen flex ${theme} text-textcolor bg-backgroundcolor`}>
            <div id="body" className="h-[100vh] w-full">
                <div id="page-content" className="ml-[65px] p-4 pr-20 h-full">
                    <Outlet/>
                </div>
            </div>
            <NavBar/>
        </div>
    )
}

export default Root;