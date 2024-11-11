import React from 'react';
import { Footer, Header } from "../component";


const StarWrapper = <P extends object>(Component: React.ComponentType<P>) => {
  // Define the HOC component that accepts props of type P.
  const HOC: React.FC<P> = (props) => {
    return (
      <>
        {/* Wrapper for the Header and Component */}
        <div className="relative z-10">
          {/* Render the Header component */}
          <Header />
          {/* Render the passed component (Component) with its props */}
          <Component {...props} />
        </div>

        {/* Wrapper for the Footer component */}
        <div className="relative z-0">
          {/* Render the Footer component */}
          <Footer />
        </div>
      </>
    );
  };
  return HOC;
};
export default StarWrapper;