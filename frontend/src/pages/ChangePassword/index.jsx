import React, { useState } from 'react';
import loginService from '../../services/loginService';
import { useNavigate } from 'react-router-dom';

const ChangePassword = () => {
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmNewPassword, setConfirmNewPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const username = "admin";

  const handleChangePassword = async () => {
    try {
      // Check if new passwords match
      if (newPassword !== confirmNewPassword) {
        setError('New passwords do not match');
        return;
      }

      await loginService.changePassword(username, oldPassword, newPassword);

      navigate('/login');
    } catch (error) {
      console.error('Error changing password:', error);
      setError('Error changing password');
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', height: '100vh', padding: '20px' }}>
      {error && (
        <div style={{ width: '100%', marginBottom: '20px', padding: '10px', background: 'red', borderRadius: '8px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)', textAlign: 'center', color: 'white' }}>
          {error}
        </div>
      )}
      <div style={{ width: '45%', marginBottom: '20px' }}>
        <div style={{ padding: '20px', borderRadius: '8px', boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)' }}>
          <div style={{ marginBottom: '15px' }}>
            <label htmlFor="old-password" style={{ display: 'block', marginBottom: '5px' }}>Old Password:</label>
            <input
              type="password"
              id="old-password"
              value={oldPassword}
              onChange={(e) => setOldPassword(e.target.value)}
              style={{ width: '100%', padding: '10px', border: '1px solid #ccc', borderRadius: '4px' }}
              required
            />
          </div>
          
          <div style={{ marginBottom: '15px' }}>
            <label htmlFor="new-password" style={{ display: 'block', marginBottom: '5px' }}>New Password:</label>
            <input
              type="password"
              id="new-password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              style={{ width: '100%', padding: '10px', border: '1px solid #ccc', borderRadius: '4px' }}
              required
            />
          </div>
          
          <div style={{ marginBottom: '15px' }}>
            <label htmlFor="confirm-new-password" style={{ display: 'block', marginBottom: '5px' }}>Confirm New Password:</label>
            <input
              type="password"
              id="confirm-new-password"
              value={confirmNewPassword}
              onChange={(e) => setConfirmNewPassword(e.target.value)}
              style={{ width: '100%', padding: '10px', border: '1px solid #ccc', borderRadius: '4px' }}
              required
            />
          </div>
          
          <button onClick={handleChangePassword} style={{ backgroundColor: '#4caf50', color: 'white', padding: '10px 20px', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>Change Password</button>
        </div>
      </div>
    </div>
  );
};

export default ChangePassword;
