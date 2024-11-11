import { iconsAd, iconsUs } from "../../constants";
import { logout, profile } from "../../assets";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { useAuth } from "../authContext/AuthContext";

// Define the NavAdmin component which accepts setActiveSection as a prop
const NavAdmin = ({ setActiveSection }) => {

  // Get the authentication context using the useAuth hook
  const auth = useAuth();

  // State to hold the user's role
  const [user, setUser] = useState('');

  // Effect to update the user's role whenever the auth.user changes
  useEffect(() => {
    // Check if the user's role is defined and differs from the current user state
    if (auth.user?.role && auth.user.role !== user) {
      setUser(auth.user.role); // Update the user state with the current role
    }
  }, [auth.user, user]); // Dependencies: effect runs whenever auth.user or user changes

  // Get the navigate function for programmatic navigation
  const navigate = useNavigate();

  // Function to handle user logout
  const Logout = () => {
    auth.logout(); // Call the logout function from the auth context
    navigate('/'); // Redirect to the home page after logging out
  };

  return (
    <div className="flex h-screen">
      <div className="bg-white h-screen text-black flex flex-col items-center py-4 w-11 hover:w-full transition-all duration-300 group/nav">
        <NavItem1 icon={profile} name={auth.user?.fullName} email={auth.user?.email} setActiveSection={setActiveSection} section={'personalInformation'} />
        {user === "user" || user === "seller" ? (
          <>
            {
              iconsUs.map((item, index) => (
                <NavItem2 key={index} icon={item.icon} text={item.text} setActiveSection={setActiveSection} section={item.section} />
              ))
            }
          </>
        ) : (
          <>
            {
              iconsAd.map((item, index) => (
                <NavItem key={index} icon={item.icon} text={item.text} setActiveSection={setActiveSection} section={item.section} dropdown={item.dropdown} />
              ))
            }
          </>
        )
        }
        <NavItem icon={logout} text={'Logout'} setActiveSection={Logout} section={'logout'} dropdown={null} />
      </div>
    </div>
  );
};

const NavItem = ({ icon, text, setActiveSection, section, dropdown }) => (
  <div className="relative group flex items-center w-full my-4 ml-6">
    <button
      onClick={() => setActiveSection(section)}
      className="flex items-center"
    >
      <img src={icon} className="flex justify-center" />
      <span className="overflow-hidden whitespace-nowrap transition-all duration-300 group-hover/nav:opacity-100 group-hover/nav:ml-4 opacity-0">
        {text}
      </span>
    </button>
    {dropdown && (
      <div className="absolute right-8 -top-9 ml-10 bg-white shadow-lg opacity-0 group-hover:opacity-100 transition-opacity duration-300">
        {dropdown.map((item, index) => (
          <button
            key={index}
            onClick={() => setActiveSection(item.section)}
            className="block w-full h-full px-4 py-2 text-left hover:bg-gray-200"
          >
            {item.text}
          </button>
        ))}
      </div>
    )}
  </div>
);


const NavItem1 = ({ icon, name, email, setActiveSection, section }) => (

  <button onClick={() => setActiveSection(section)} className="flex items-center w-full my-4 ml-0.5 border-b pb-4 border-gray-400">
    <img src={icon} className="flex justify-center " />
    <span className="flex flex-col mb-1">
      <span className="flex overflow-hidden ml-4 whitespace-nowrap transition-all duration-300 group-hover/nav:opacity-100 opacity-0">
        {name}
      </span>
      <span className="overflow-hidden whitespace-nowrap transition-all duration-300 group-hover/nav:opacity-100 group-hover/nav:ml-4 opacity-0">
        {email}
      </span>
    </span>
  </button>
);

const NavItem2 = ({ icon, text, setActiveSection, section }) => (
  <button onClick={() => setActiveSection(section)} className="flex items-center w-full my-4 ml-6">
    <img src={icon} className="flex justify-center " />
    <span className="flex flex-col mb-1">
      <span className="flex overflow-hidden ml-4 whitespace-nowrap transition-all duration-300 group-hover/nav:opacity-100 opacity-0">
        {text}
      </span>
    </span>
  </button>
);

export default NavAdmin;
