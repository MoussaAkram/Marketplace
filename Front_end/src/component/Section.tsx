import React from 'react'
import { Outlet } from 'react-router-dom';
import { CartProvider, SearchProvider } from '../lib';

const Section = () => {

  // Render the component wrapped in context providers
  return (
    // Provides cart-related state and functionality to the children components
    <CartProvider>
      {/* Provides search-related state and functionality to the children components */}
      <SearchProvider>
        {/* Renders the matched child component from the routing system */}
        <Outlet />
      </SearchProvider>
    </CartProvider>
  )
}

export default Section