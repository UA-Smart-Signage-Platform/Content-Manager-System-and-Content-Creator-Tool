import { Navigate } from "react-router";
import { useUserStore } from "../../stores/useUserStore";
import PropTypes from 'prop-types';


function ProtectedRoute({ element, requiredRole }){
    const userRole = useUserStore((state)=> state.role)
    
    
    if (userRole === requiredRole){
        return element
    }else{
        <Navigate to="/monitors"/>
    }
  
}

ProtectedRoute.propTypes = {
    element: PropTypes.element.isRequired,
    requiredRole: PropTypes.string.isRequired,
}

export default ProtectedRoute;