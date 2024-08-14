import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import {Navigate, createBrowserRouter, RouterProvider } from 'react-router-dom';
import { Root, Dashboard, Admin, ChangePassword, Wso2Login, Monitors, Media, Monitor, Schedule, Templates, Cct, Logs } from './routes';
import PropTypes from 'prop-types';

const ProtectedRoute = ({ element, requiredRoles }) => {
  const userRoles = localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')).role : null;
  const hasRequiredRole = userRoles && requiredRoles.some(role => userRoles.includes(role));

  return hasRequiredRole ? element : <Navigate to="/dashboard" />;
};

ProtectedRoute.propTypes = {
  element: PropTypes.element.isRequired,
  requiredRoles: PropTypes.arrayOf(PropTypes.string).isRequired
};

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root/>,
    children:[
      {
        path: "dashboard",
        element:<Dashboard/>
      },
      {
        path: "dashboard/logs",
        element:<Logs/>
      },
      {
        path: "media/*",
        element:<Media/>,
      },
      {
        path: "schedule",
        element:<Schedule/>,
      },
      {
        path: "monitors",
        element:<Monitors/>,
      },
      {
        path: "monitor/:id",
        element:<Monitor/>,
      },
      {
        path: "contentcreator",
        element: <ProtectedRoute element={<Templates/>} requiredRoles={["ROLE_ADMIN"]} />
      },
      {
        path: "login",
        element:<Wso2Login/>,
      },
      {
        path: "",
        element:<Wso2Login/>
      },
      {
        path: "admin",
        element: <ProtectedRoute element={<Admin/>} requiredRoles={["ROLE_ADMIN"]} />
      },
      {
        path: "change-password",
        element: <ProtectedRoute element={<ChangePassword/>} requiredRoles={["ROLE_ADMIN"]} />
      },
      {
        path: "contentcreator/:id",
        element:<Cct/>
      }
    ]
  },
])

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <RouterProvider router={router}/>
  </React.StrictMode>
);
