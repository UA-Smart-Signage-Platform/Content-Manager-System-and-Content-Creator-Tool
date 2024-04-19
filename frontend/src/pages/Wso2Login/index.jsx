import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from "react-router-dom";

// Define the required constants
const CLIENT_ID = process.env.REACT_APP_WSO2_CLIENT_ID;
const REDIRECT_URI = process.env.REACT_APP_WSO2_REDIRECT_URI;
const IDP_URI = process.env.REACT_APP_IDP_URI;

const redirectToLogin = () => {
  window.location.replace(`${IDP_URI}/authorize?response_type=code&client_id=${CLIENT_ID}&state=1234567890&scope=openid&redirect_uri=${REDIRECT_URI}`);
};



const Wso2Login = () => {
  

  return (
    <div>
      {/* Your component UI here */}
      <button onClick={redirectToLogin}>Login</button>
    </div>
  );
};

export default Wso2Login;
