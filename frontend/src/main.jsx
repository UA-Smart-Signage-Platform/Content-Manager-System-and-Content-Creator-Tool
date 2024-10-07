import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import {createBrowserRouter, RouterProvider } from 'react-router-dom';
import {ProtectedRoute, Root, Dashboard, Admin, 
        ChangePassword, Wso2Login, Monitors, 
        Media, Monitor, Schedule, Templates, 
        Cct, MonitorLogs, ServerLogs} from './routes';

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
        path: "dashboard/logs/backend",
        element:<ServerLogs/>
      },
      {
        path: "dashboard/logs/monitors",
        element:<MonitorLogs/>
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
        element: <ProtectedRoute element={<Templates/>} requiredRole={"ROLE_ADMIN"} />
      },
      {
        path: "login",
        element:<Wso2Login/>,
      },
      {
        path: "admin",
        element: <ProtectedRoute element={<Admin/>} requiredRole={"ROLE_ADMIN"} />
      },
      {
        path: "change-password",
        element: <ProtectedRoute element={<ChangePassword/>} requiredRole={"ROLE_ADMIN"} />
      },
      {
        path: "contentcreator/:id",
        element:<ProtectedRoute element={<Cct/>} requiredRole={"ROLE_ADMIN"} />
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
