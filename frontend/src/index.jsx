import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';
import {Navigate, createBrowserRouter, RouterProvider } from 'react-router-dom';
import { Root } from './routes';
import { Dashboard, Monitors, Media, Monitor, Schedule, Wso2Login, Admin, Templates, ChangePassword } from './pages';
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
    ]
  },
])

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <RouterProvider router={router}/>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
