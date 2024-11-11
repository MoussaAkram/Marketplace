import { useState } from 'react';
import NavAdmin from './Nav/NavAdmin';

const ActiveSection = (WrappedComponent) => {
    // Define the HOC component.
    const HOC = (props) => {
        // Initialize state to manage the active section, defaulting to 'personalInformation'.
        const [activeSection, setActiveSection] = useState('personalInformation');

        // Return the JSX structure for the HOC.
        return (
            <div className="flex">
                {/* Render the NavAdmin component, passing setActiveSection as a prop to allow it to update the active section. */}
                <NavAdmin setActiveSection={setActiveSection} />

                {/* Render the WrappedComponent, spreading the original props and passing down activeSection and setActiveSection as props. */}
                <div className="flex-1">
                    <WrappedComponent
                        {...props} // Spread the incoming props to the WrappedComponent.
                        activeSection={activeSection} // Pass the current active section.
                        setActiveSection={setActiveSection} // Pass the function to update the active section.
                    />
                </div>
            </div>
        );
    };

    return HOC;
};

export default ActiveSection;
