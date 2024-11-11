import React, { useState, useEffect } from 'react';
import "./scroll.model.css"


const ScrollTop = () => {
    const [isVisible, setIsVisible] = useState(false);
    const [bottomOffset, setBottomOffset] = useState(30);

    // Function to toggle visibility based on scroll position
    const toggleVisibility = () => {
        // Get the height of the footer element, defaulting to 0 if not found
        const footerHeight = document.querySelector('footer')?.clientHeight || 0;
        // Get the height of the window
        const windowHeight = window.innerHeight;
        // Get the total height of the page content
        const pageHeight = document.documentElement.scrollHeight;
        // Get the current scroll position from the top
        const scrollPosition = window.scrollY;

        // Show the button if scrolled more than 300 pixels
        if (scrollPosition > 300) {
            setIsVisible(true);
        } else {
            setIsVisible(false);
        }

        // Calculate the distance from the bottom of the page
        const distanceFromBottom = pageHeight - (scrollPosition + windowHeight);
        // If the user is near the bottom of the page, adjust the button's position
        if (distanceFromBottom < footerHeight + 30) {
            setBottomOffset(footerHeight + 30 - distanceFromBottom);
        } else {
            // Reset the offset to the default value
            setBottomOffset(30);
        }
    };

    // Function to scroll smoothly to the top of the page
    const scrollToTop = () => {
        window.scrollTo({
            top: 0, // Scroll to the top
            behavior: 'smooth', // Smooth scrolling effect
        });
    };

    // Effect to add and clean up the scroll event listener
    useEffect(() => {
        // Add scroll event listener to the window
        window.addEventListener('scroll', toggleVisibility);
        // Cleanup function to remove the event listener when the component unmounts
        return () => {
            window.removeEventListener('scroll', toggleVisibility);
        };
    }, []);

    return (
        <div
            className="scroll-to-top"
            style={{ bottom: `${bottomOffset}px` }}
        >
            {isVisible && (
                <div onClick={scrollToTop} className="scroll-button">
                    ↑
                </div>
            )}
        </div>
    );
};

export default ScrollTop;
