import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import { createBrowserRouter,RouterProvider } from 'react-router-dom';
import { Root } from './routes';
import { Dashboard } from './pages';


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
        path: "media/:path",
        element:<div>test2</div>,
      },
      {
        path: "schedule",
        element:<div>test3</div>,
      },
      {
        path: "monitors",
        element:<div>test6</div>,
      },
      {
        path: "monitor/:id",
        element:<div>test5</div>,
      },
      {
        path: "contentcreator",
        element:<div>test4</div>,
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
