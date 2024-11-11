import { lazy, Suspense } from 'react';
import { SectionWrapper, ActiveSection, ScrollTop } from '../../lib';


// Importing components lazily using React's `lazy` function. 
// Lazy loading allows the components to be loaded only when needed, improving performance.
const PersonalInformation = lazy(() => import('./personalInformation/PersonalInformation'));
const Orders = lazy(() => import('./orders/Orders'));
const Category = lazy(() => import('./category/Category'));
const Products = lazy(() => import('./products/Products'));
const Users = lazy(() => import('./users/Users'));
const Admins = lazy(() => import('./users/Admins'));
const Sellers = lazy(() => import('./users/Sellers'));

// The `MainAdmin` component handles which section to display based on the `activeSection` prop.
const MainAdmin = ({ activeSection }) => {
  let ActiveComponent;  // Variable to hold the component that will be rendered.

  // Using a switch statement to select the appropriate component based on `activeSection`.
  switch (activeSection) {
    case 'personalInformation':
      ActiveComponent = PersonalInformation;  // Renders the PersonalInformation component.
      break;
    case 'orders':
      ActiveComponent = Orders;  // Renders the Orders component.
      break;
    case 'category':
      ActiveComponent = Category;  // Renders the Category component.
      break;
    case 'products':
      ActiveComponent = Products;  // Renders the Products component.
      break;
    case 'users':
      // Renders a placeholder message when the 'users' section is active but no specific sub-section is selected.
      ActiveComponent = () => <div>Select a sub-section</div>;
      break;
    case 'usersTable':
      ActiveComponent = Users;  // Renders the Users component.
      break;
    case 'adminsTable':
      ActiveComponent = Admins;  // Renders the Admins component.
      break;
    case 'sellersTable':
      ActiveComponent = Sellers;  // Renders the Sellers component.
      break;
    default:
      // Fallback in case the `activeSection` does not match any of the specified cases.
      ActiveComponent = () => <div>Section not found</div>;
  }

  return (
    <div className='p-4 bg-gray-200 h-full'>
      {/* Suspense component to handle the loading state while the lazy-loaded components are being fetched. */}
      <Suspense fallback={<div>Loading...</div>}>
        {/* Renders the selected component dynamically. */}
        <ActiveComponent />
        {/* ScrollTop component (presumably to scroll to the top of the page after component change). */}
        <ScrollTop />
      </Suspense>
    </div>
  );
};

// `SectionWrapper` and `ActiveSection` are assumed higher-order components (HOCs) 
// that enhance the `MainAdmin` component. The final component is exported as `Main`.
const Main = SectionWrapper(ActiveSection(MainAdmin));

export default Main;
