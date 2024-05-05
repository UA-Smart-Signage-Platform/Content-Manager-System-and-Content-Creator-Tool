import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import { createBrowserRouter,RouterProvider } from 'react-router-dom';
import { Root } from './routes';
import { Dashboard, Monitors, Media, Monitor, Schedule, Templates, Cct } from './pages';


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
        element:<Templates/>,
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