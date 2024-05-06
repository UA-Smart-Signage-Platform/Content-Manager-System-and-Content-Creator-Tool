import { NavBar } from "../../components";
import { Outlet } from "react-router";
import { useThemeStore } from "../../stores/useThemeStore";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import loginService from "../../services/loginService";


const CLIENT_ID = process.env.REACT_APP_WSO2_CLIENT_ID;
const REDIRECT_URI = process.env.REACT_APP_WSO2_REDIRECT_URI;
const IDP_URI = process.env.REACT_APP_IDP_URI;
const BASE64 = process.env.REACT_APP_IDP_BASE64;

import { createTheme } from "react-data-table-component";


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

function Root(){
    const theme = useThemeStore((state) =>state.theme)

    const location = useLocation();
    const navigate = useNavigate();
    const [currentPath, setCurrentPath] = useState(location.pathname);

    const getInfo = async () => {
        try {
          const response = await loginService.getInfo();
          console.log(response.data);
          localStorage.setItem('userInfo', JSON.stringify(response.data));
          return response.data;
        } catch (error) {
          console.error('Error getting info:', error);
        }
      };

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
            
            try {
              
                setWithExpiry('access_token', data.access_token, seconds * 1000)
            
              const permissions = await getInfo();
              console.log("permissions", permissions);
              
              if (!permissions || permissions.error || permissions === undefined) {
                  console.log("No permissions or error");
                  localStorage.removeItem('access_token');
                  navigate("/login");
                  return;
              }
  
              
                setWithExpiry('access_token', data.access_token, seconds * 1000)
                setWithExpiry('id_token', data.id_token, seconds * 1000)
                localStorage.setItem('refresh_token', data.refresh_token)
              
              
                navigate("/dashboard");
  
          } catch (error) {
              console.error('Error getting info:', error);
              localStorage.removeItem('access_token');
              console.log(localStorage.getItem('access_token'));
              navigate("/login");
          }
          
          });
      };

      const getRefreshedToken = async () => {
        const refresh_token = localStorage.getItem('refresh_token');
        console.log("refresh_token", refresh_token);
        if (!refresh_token) {
            navigate("/login");
            return;
        }
        
        try {
            const response = await fetch(`${IDP_URI}/token?refresh_token=${refresh_token}&grant_type=refresh_token`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    Authorization: `${BASE64}`
                }
            });
    
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
    
            const data = await response.json();
            console.log("refreshed data", data);
            
            let seconds = data.expires_in;
    
            
            setWithExpiry('access_token', data.access_token, seconds * 1000)
            setWithExpiry('id_token', data.id_token, seconds * 1000)
            localStorage.setItem('refresh_token', data.refresh_token)
            


    
        } catch (error) {
            console.error("Error refreshing token:", error);
            
        }

        return localStorage.getItem('access_token');
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
        console.log(item);
        const now = new Date();
        console.log("time: ", now.getTime());
        if (now.getTime() > item.expiry) {
          return "";
        }
        return item.value;
      };

      useEffect(() => {
        setCurrentPath(location.pathname);
    
        const handleToken = async () => {
            try {
                const token = await getWithExpiry("access_token");
                const params = new URLSearchParams(location.search);
                const params_code = params.get('code');

                console.log("currentPath", currentPath);
                
                if (!token && !params_code && currentPath !== '/') {
                    console.log("blablabla");
                    await getRefreshedToken();
                    const newToken = await getWithExpiry("access_token");
                    
                    if (!newToken) {
                        navigate("/login");
                    }
                } else if (params_code && !token) {
                    getAccessToken(params_code);
                }
            } catch (error) {
                console.error("Error handling token:", error);
                redirectToLogin();
            }
        };
    
        handleToken();
    }, [location.pathname, location.search]);
    

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