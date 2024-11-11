import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';


interface GuardedRouteProps {
    isPaymentComplete: boolean;
    redirectTo: string;
}

// Define the GuardSuccess component as a functional component
const GuardSuccess: React.FC<GuardedRouteProps> = ({ isPaymentComplete, redirectTo }) => {
    const paymentStatus = sessionStorage.getItem('paymentStatus'); 

    // Check if the payment is complete or if the payment status indicates success or failure
    return (isPaymentComplete || paymentStatus === 'success' || paymentStatus === 'failed') 
      ? <Outlet />
      : <Navigate to={redirectTo} />;
  };


export default GuardSuccess;