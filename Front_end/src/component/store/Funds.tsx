import React, { useEffect, useState } from 'react'
import urls from '../../services/urls';
import { useAuth } from '../../lib';

const Funds = ({ setSold, sold, showPrice, togglePriceModel, process, setProcess }) => {
    const [recipientEmail, setRecipientEmail] = useState('');
    const [amount, setAmount] = useState('');
    const [email, setEmail] = useState('');
    const [sellerId, setSellerId] = useState(null);
    const [isDataFetched, setIsDataFetched] = useState(false);
    const { user } = useAuth();

    // useEffect to fetch user data when the user ID is available and data hasn't been fetched yet
    useEffect(() => {
        // Check if user ID exists and if data has not been fetched
        if (user?.id && !isDataFetched) {
            // Async function to fetch user data
            const fetchUserData = async () => {
                try {
                    // Fetch seller data by user ID from the API
                    const response = await urls.getSellerById(user.id);
                    const userData = response.data;

                    // Update state with the fetched user data
                    setRecipientEmail(userData.paypalEmail); // Set the recipient's email
                    setEmail(userData.paypalEmail);          // Also set the email in another state
                    setSellerId(userData.id);                 // Set the seller ID
                    setIsDataFetched(true);                    // Mark data as fetched
                } catch (error) {
                    // Log error if fetching user data fails
                    console.error('Error fetching user data:', error);
                }
            };

            // Call the fetchUserData function
            fetchUserData();
        }
        // Dependency array: re-run effect if user ID, role, or isDataFetched changes
    }, [user?.id, user?.role, isDataFetched]);

    // Function to handle form submission
    const onSubmit = (e) => {
        e.preventDefault(); // Prevent default form submission behavior

        // Prepare the payload for the API request
        const payload = {
            recipientEmail, // Email of the recipient
            amount,         // Amount to be processed
            email,          // User's email
            sellerId        // ID of the seller
        };

        // Send payment request
        urls.getPayed(payload)
            .then(async () => {
                setProcess('successfull'); // Set process state to 'successful'
                // Fetch updated seller data after payment
                const response = await urls.getSellerById(user?.id);
                const userData = response.data;
                setSold(userData.sold);   // Update sold items count
                setAmount('');             // Reset amount field after submission
            })
            .catch(() => {
                setProcess('failed'); // Set process state to 'failed' on error
            });
    };

    // Function to handle changes in the input field
    const handleChange = (e) => {
        const inputValue = e.target.value; // Get the value from the input field

        // Regular expression to allow only numbers with a period for up to 2 decimal places
        if (inputValue === '' || /^\d+(\.\d{0,2})?$/.test(inputValue)) {
            setAmount(inputValue); // Update the amount state if input is valid
        }
    };

    return (
        <>
            <div className={` z-20 w-full ${showPrice ? '' : 'hidden'}`}>
                <div className="h-screen w-screen bg-black opacity-50 fixed top-0 left-0"></div>
                <div id="priceModal" tabIndex={-1} className="flex justify-center content-center fixed top-0 overflow-y-auto overflow-x-hidden right-0 left-0 z-50  items-center w-full md:inset-0 md:h-full">
                    <div className="relative p-4 w-full max-w-xl h-full md:h-auto">
                        <div className="relative p-4 bg-white rounded-lg shadow sm:p-5">
                            <button onClick={togglePriceModel} type="button" className="text-gray-400 absolute top-2.5 right-2.5 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center" data-modal-toggle="priceModal">
                                <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd"></path></svg>
                                <span className="sr-only">Close modal</span>
                            </button>
                            {process == 'process' ? (
                                <>
                                    <h1 className="font-bold text-xl leading-10 text-black mt-4">Your Funds: ${sold}</h1>
                                    <form onSubmit={onSubmit}>
                                        <div className="mb-8 mt-4">
                                            <label htmlFor="input_amount" className="block mb-2 font-medium text-gray-900">Amount:</label>
                                            <input type="number" step="0.01" id="input_amount" name='amount' value={amount} onChange={handleChange} className="border border-gray-400 text-gray-900 text-base rounded-lg w-full block px-5 py-2" placeholder="0.0" required />
                                        </div>
                                        <div className="flex justify-center items-center space-x-4">
                                            <button type="submit" className="text-black border border-black text-sm font-semibold hover:bg-black hover:text-white focus:ring-4 focus:outline-none focus:ring-purple-300 shadow-lg shadow- shadow-slate-800/50  rounded-lg px-5 py-2.5 text-center me-2 mb-2">Withdraw your funds</button>
                                        </div>
                                    </form>
                                </>
                            ) : process == 'successfull' ? (
                                <div>
                                    <div className="w-12 h-12 rounded-full bg-green-100 dark:bg-green-900 p-2 flex items-center justify-center mx-auto mb-3.5">
                                        <svg aria-hidden="true" className="w-8 h-8 text-green-500 dark:text-green-400" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd"></path></svg>
                                        <span className="sr-only">Success</span>
                                    </div>
                                    <p className="flex items-center justify-center mb-4 text-lg font-semibold text-emerald-500 dark:text-white">Successfull</p>
                                </div>

                            ) : process == 'failed' ? (
                                <div>
                                    <div className="w-12 h-12 rounded-full bg-red-100 p-2 flex items-center justify-center mx-auto mb-3.5">
                                        <svg className="w-20 h-20 text-red-600 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"> <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                                    </div>
                                    <p className="flex items-center justify-center mb-4 text-lg font-semibold text-red-500 dark:text-white">Failed</p>
                                </div>
                            ) : null
                            }
                        </div>
                    </div>
                </div>
            </div>

        </>
    )
}

export default Funds