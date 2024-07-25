import React, { useState, useEffect } from 'react';
import loginService from '../../services/loginService';
import { useNavigate } from "react-router-dom";

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
  const [error, setError] = useState('');
  const navigate = useNavigate();

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

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', height: '100vh', padding: '20px' }}>
      <div style={{ width: '45%', marginBottom: '20px' }}>
        <div style={{ padding: '20px', borderRadius: '8px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
          <h2>Login</h2>
          <div style={{ marginBottom: '15px' }}>
            <label htmlFor="username" style={{ display: 'block', marginBottom: '5px' }}>Username:</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              style={{ width: '100%', padding: '10px', border: '1px solid #ccc', borderRadius: '4px' }}
              required
            />
          </div>
          <div style={{ marginBottom: '15px' }}>
            <label htmlFor="password" style={{ display: 'block', marginBottom: '5px' }}>Password:</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={{ width: '100%', padding: '10px', border: '1px solid #ccc', borderRadius: '4px' }}
              required
            />
          </div>
          {error && (
            <div style={{ color: 'red', marginBottom: '15px' }}>
              {error}
            </div>
          )}

          <button onClick={handleLogin} style={{ backgroundColor: '#4caf50', color: 'white', padding: '10px 20px', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>Login</button>
        </div>
      </div>

      <div style={{ width: '45%' }}>
        <div style={{ padding: '20px', borderRadius: '8px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
          <h2>UA Login</h2>
          <button onClick={redirectToLogin} style={{ backgroundColor: '#5a67d8', color: 'white', padding: '10px 20px', border: 'none', borderRadius: '4px', cursor: 'pointer', marginTop: '10px' }}>UA Login</button>
        </div>
      </div>
    </div>
  );
};

export default Wso2Login;
