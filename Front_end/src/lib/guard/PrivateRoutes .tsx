import { Outlet, Navigate } from 'react-router-dom';
import { useAuth } from '../authContext/AuthContext';
import Loading from '../loading/Loading';


// Define the PrivateRoutes component which accepts allowedRoles as a prop
const PrivateRoutes = ({ allowedRoles }) => {
    // Get the authentication context using the useAuth hook
    const auth = useAuth();

    // If authentication is still loading, show the loading component
    if (auth.loading) {
        return <Loading />;
    }

    // If the user is not logged in, redirect to the home page
    if (!auth.isLoggedIn) {
        return <Navigate to="/" />;
    }

    // Get the user's role from the authentication context
    const rol = auth.user?.role;

    // If the user's role is not in the allowed roles, redirect to the home page
    if (!allowedRoles.includes(rol)) {
        return <Navigate to="/" />;
    }

    // If all checks pass, render the child routes
    return <Outlet />;
};

// Export the PrivateRoutes component for use in other parts of the application
export default PrivateRoutes;