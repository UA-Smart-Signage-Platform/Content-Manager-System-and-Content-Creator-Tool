import React, { useState, useEffect, useMemo } from 'react';
import Particles, { initParticlesEngine } from "@tsparticles/react";
import { loadFull } from "tsparticles";
import loginService from '../../services/loginService';
import { useNavigate } from "react-router-dom";
import { greenParticles,nyanParticles } from './particlesConfigs';
import SignInDiv from './SignInDiv';

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
  const [nyan,setNyan] = useState(false);
  const [init, setInit] = useState(false);

  useEffect(() => {
    initParticlesEngine(async (engine) => {
      await loadFull(engine);
    }).then(() => {
      setInit(true);
    });
  }, []);

  const particlesLoaded = (container) => {
  };

  const options = useMemo(()=> {
      if(nyan) {
        return nyanParticles
      }
      else {
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
        <SignInDiv setNyan={setNyan}/>
      </div>
    );
  }
};

export default Wso2Login;
