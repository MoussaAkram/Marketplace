import { lazy, Suspense } from 'react';
import { SectionWrapper, ActiveSection, ScrollTop } from '../../lib';

// Importing components lazily using React's `lazy` function. 
// Lazy loading allows the components to be loaded only when needed, improving performance.
const PersonalInformation = lazy(() => import('./personalInformation/PersonalInformation'));
const Orders = lazy(() => import('./orders/Orders'));
const Purchases = lazy(() => import('./purchases/Purchases'));


// The `MainAdmin` component handles which section to display based on the `activeSection` prop.
const Main = ({ activeSection }) => {

  let ActiveComponent; // Variable to hold the component that will be rendered.

  // Using a switch statement to select the appropriate component based on `activeSection`.
  switch (activeSection) {
    case 'personalInformation':
      ActiveComponent = PersonalInformation;
      break;
    case 'myOrders':
      ActiveComponent = Orders;
      break;
    case 'purchases':
      ActiveComponent = Purchases;
      break;
    default:
      ActiveComponent = () => <div>Section not found</div>;
  }

  return (
    <div className='p-4 bg-gray-200 h-full'>
      <Suspense fallback={<div>Loading...</div>}>
        <ActiveComponent  />
        <ScrollTop />
      </Suspense>
    </div>
  );
};
const WrappedMain = SectionWrapper(ActiveSection(Main));

export default WrappedMain;