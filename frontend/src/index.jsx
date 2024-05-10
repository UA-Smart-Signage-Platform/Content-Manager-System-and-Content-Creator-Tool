import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Root } from './routes';
import { Dashboard, Monitors, Media, Monitor, Schedule, Wso2Login, Admin, Templates, ChangePassword } from './pages';

const ProtectedRoute = ({ element, requiredRoles }) => {
  const userRoles = localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')).role : null;
  const hasRequiredRole = userRoles && requiredRoles.some(role => userRoles.includes(role));

  return hasRequiredRole ? element : <Navigate to="/login" />;
};

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Root />}>
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="media/*" element={<Media />} />
          <Route path="schedule" element={<Schedule />} />
          <Route path="monitors" element={<Monitors />} />
          <Route path="monitor/:id" element={<Monitor />} />
          <Route path="contentcreator" element={<ProtectedRoute element={<Templates />} requiredRoles={['ROLE_ADMIN']} />} />
          <Route path="login" element={<Wso2Login />} />
          <Route path="admin" element={<ProtectedRoute element={<Admin />} requiredRoles={['ROLE_ADMIN']} />} />
          <Route path="change-password" element={<ProtectedRoute element={<ChangePassword />} requiredRoles={['ROLE_ADMIN']} />} />
          <Route path="" element={<Wso2Login />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
