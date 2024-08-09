import React, { useState, useEffect, useMemo } from 'react';
import Particles, { initParticlesEngine } from "@tsparticles/react";
import { loadFull } from "tsparticles";
import loginService from '../../services/loginService';
import { useNavigate } from "react-router-dom";
import  Logo  from '../../static/green-name.svg?react';
import { greenParticles,nyanParticles } from './particlesConfigs';

const CLIENT_ID = import.meta.env.REACT_APP_WSO2_CLIENT_ID;
const REDIRECT_URI = import.meta.env.REACT_APP_WSO2_REDIRECT_URI;
const IDP_URI = import.meta.env.REACT_APP_IDP_URI;
const DEFAULT_PASSWORD = import.meta.env.REACT_APP_DEFAULT_PASSWORD;

const redirectToLogin = () => {
  window.location.replace(`${IDP_URI}/authorize?response_type=code&client_id=${CLIENT_ID}&state=1234567890&scope=openid&redirect_uri=${REDIRECT_URI}`);
};

const setWithExpiry = (key, value, ttl) => {
  const now = new Date();
  const item = {
    value: value,
    expiry: now.getTime() + ttl,
  };
  localStorage.setItem(key, JSON.stringify(item));
};

const Wso2Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loginRoot,setLoginRoot] = useState(false);
  const [nyan,setNyan] = useState(0);
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const [init, setInit] = useState(false);

  useEffect(() => {
    initParticlesEngine(async (engine) => {
      await loadFull(engine);
    }).then(() => {
      setInit(true);
    });
  }, []);

  const particlesLoaded = (container) => {
    console.log(container);
  };

  useEffect(() => {
    localStorage.removeItem('access_token');
    localStorage.removeItem('id_token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem('userInfo');
  }, []);

  const handleLogin = async () => {
    try {
      const response = await loginService.login(username, password);
      const jwt = response.data.jwt;
      const user_data = { username: response.data.username, role: response.data.role };

      setWithExpiry('access_token', jwt, 3600 * 1000 * 10);
      localStorage.setItem('userInfo', JSON.stringify(user_data));

      if (password === DEFAULT_PASSWORD) {
        navigate("/change-password");
      } else {
        navigate("/dashboard");
      }
    } catch (error) {
      setError('Invalid username or password');
      console.error('Error logging in:', error.response?.data || error.message);
    }
  };

  const options = useMemo(
    ()=> {
      if(nyan > 9){
        return nyanParticles
      }else{
        return greenParticles
      }
    },
    [nyan],
  );
  if(init){
    return (
      <div className='h-full w-full relative'>
        <Particles
          id="tsparticles"
          className='h-full w-full absolute'
          style={{zIndex:-1}}
          particlesLoaded={particlesLoaded}
          options={options}
        />
        <div className='flex flex-col gap-20 w-[50%] mx-auto items-center z-40 pt-[10%] relative'>
          <div onClick={()=>setNyan(nyan+1)}><Logo></Logo></div>
          {loginRoot ? 
          <div className='flex flex-col gap-6 items-center justify-center'>
            <div className='w-full'>
              <p className=' justify-start w-full text-2xl text-[#95A967]'>Sign in as Root</p>
              <input className=' border-secondary border-2 rounded-md font-bold p-1 text-xl bg-secondaryLight'
                    placeholder='password'
              />
            </div>
            <button className='bg-primary font-bold text-lg p-1 rounded-md w-full'>Sign In</button>
            <button className='bg-secondary font-bold text-lg p-1 rounded-md w-[75%]'
                    onMouseDown={()=> setLoginRoot(!loginRoot)}>
                    Back
            </button>
          </div>
          : 
          <div className='flex flex-col gap-6 items-center justify-center'>
            <button className='bg-primary font-bold text-xl p-3 rounded-md px-16'>Sign in with IDP</button>
            <button className='bg-[#95A967] font-bold text-lg p-1 rounded-md w-[75%]'
                    onMouseDown={()=> setLoginRoot(!loginRoot)}>
                    Admin Sign in
            </button>
          </div>
          }
        </div>
      </div>
    );
  }
};

export default Wso2Login;
