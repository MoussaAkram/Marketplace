import { useEffect, useState } from 'react';
import { country } from '../../../constants'
import { Toastify, Notify, useAuth, ErrorNotify } from '../../../lib'
import { Seller } from '../../../interface/interface';
import urls from '../../../services/urls';


const PersonalInformation = () => {

    const [updateForm, setUpdateForm] = useState<Seller>({ fullName: '', phone: '', country: '', address: '', businessName: '', businessEmail: '', businessAddress: '', paypalEmail: '' });
    const [isDataFetched, setIsDataFetched] = useState(false);
    const { user, updateUser } = useAuth()


    // useEffect to fetch user data when the component mounts or when the `user.id`, `user.role`, or `isDataFetched` values change.
    useEffect(() => {
        // Check if user ID exists and data hasn't been fetched yet.
        if (user?.id && !isDataFetched) {
            // Async function to fetch user data based on the user's role.
            const fetchUserData = async () => {
                try {
                    // If the user has a role, determine which API to call based on whether the user is a seller or a regular user.
                    if (user?.role) {
                        const response = user.role === 'seller'
                            ? await urls.getSellerById(user.id)  // Fetch seller data if user is a seller.
                            : await urls.getUserById(user.id);  // Fetch regular user data if user is not a seller.

                        const userData = response.data;

                        // Populate the form with the fetched user data, using default empty values if any field is missing.
                        setUpdateForm({
                            id: userData.id || '',
                            fullName: userData.fullName || '',
                            phone: userData.phone || '',
                            country: userData.country || '',
                            address: userData.address || '',
                            businessName: userData.businessName || '',
                            businessEmail: userData.businessEmail || '',
                            businessAddress: userData.businessAddress || '',
                            paypalEmail: userData.paypalEmail || ''
                        });

                        // Set the flag indicating that data has been successfully fetched.
                        setIsDataFetched(true);
                    }
                } catch (error) {
                    // Log any errors encountered during the data fetching process.
                    console.error('Error fetching user data:', error);
                }
            };

            // If a user ID exists, invoke the `fetchUserData` function to load the data.
            if (user?.id) {
                fetchUserData();
            }
        }
    }, [user?.id, user?.role, isDataFetched]);  // Dependencies: Only re-run if any of these values change.

    // Another useEffect that runs when `isDataFetched` becomes true (after data has been fetched).
    useEffect(() => {
        if (isDataFetched) {
            // Update the user's data once the form is populated with the fetched information.
            const updateData = {
                fullName: updateForm.fullName,  // For now, only the full name is updated.
            };
            updateUser(updateData);  // Function to handle updating the user data.
        }
    }, [isDataFetched]);  // Dependency: Runs whenever `isDataFetched` changes.

    // Async function to handle updating the user data via an API call.
    const updateUserData = async () => {
        try {
            // Determine which API to call based on whether the user is a seller or not.
            const response = user?.role === 'seller'
                ? await urls.updateSeller(updateForm)  // Update seller data.
                : await urls.updateUser(updateForm);  // Update regular user data.

            // Prepare the data to be updated, here only `fullName` is updated.
            const updateData = {
                fullName: updateForm.fullName,
            };

            // Log the response for debugging purposes.
            console.log(response.data);

            // Invoke the `updateUser` function to update the state/UI.
            updateUser(updateData);

            // Notify the user that the information was updated successfully.
            Notify('Information updated successfully');
        } catch (error) {
            // Log any errors encountered during the update process.
            console.error('Error updating data:', error);

            // Notify the user of an error, such as if the name is already in use.
            ErrorNotify('This name is already used');
        }
    };

    // Handler for form submission to trigger the update process.
    const updateSubmit = (e: React.FormEvent) => {
        e.preventDefault();  // Prevent the default form submission behavior.
        updateUserData();  // Call the function to update user data.
    };

    // Handler for form input changes, dynamically updates the `updateForm` state as the user types.
    const updateChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;  // Destructure the name and value from the event target.
        setUpdateForm((prev) => ({
            ...prev,
            [name]: value  // Update the respective form field with the new value.
        }));
    };



    return (
        <>
            <div className="relative grid ">
                <div className="flex flex-col sm:flex-row items-center md:items-start sm:justify-center md:justify-start flex-auto min-w-0 ">
                    <div className="md:flex md:items-center md:justify-left w-full sm:w-auto md:h-full xl:w-1/2 p-8  md:p-10 lg:p-14 sm:rounded-lg md:rounded-none ">
                        <div className="max-w-xl w-full space-y-12">
                            <h1 className='font-semibold text-3xl'>Personal Information</h1>
                            <div className="lg:text-left text-center">
                                <div className="flex items-center justify-center ">
                                    <div className="flex flex-col w-full ">
                                        <form onSubmit={updateSubmit} className="flex flex-col space-y-2 ">
                                            <label htmlFor='fullName_input' className="font-medium text-lg text-black" >Full Name:</label>
                                            <input id="fullName_input" name='fullName' type="text" placeholder="Full Name" value={updateForm.fullName || ''} onChange={updateChange} className="border border-black rounded-lg py-3 px-3 mt-2 bg-white placeholder-white-500 text-black" />
                                            <label htmlFor='address_input' className="font-medium text-lg text-black" >Address:</label>
                                            <input id="address_input" name='address' type="text" placeholder="address" value={updateForm.address || ''} onChange={updateChange} className="border border-black rounded-lg py-3 px-3 mt-2 bg-white placeholder-white-500 text-black" />
                                            {user?.role === 'seller' && (
                                                <>
                                                    <label htmlFor='paypal_input' className="font-medium text-lg text-black">Paypal Email:</label>
                                                    <input id="paypal_input" name='paypalEmail' value={updateForm.paypalEmail || ''} onChange={updateChange} type="email" placeholder="Paypal" className="border border-black rounded-lg py-3 px-3 bg-white placeholder-white-500 text-black" />
                                                    <label htmlFor='businessName_input' className="font-medium text-lg text-black" >Business Name:</label>
                                                    <input id="businessName_input" name='businessName' type="text" placeholder="Business Name" value={updateForm.businessName || ''} onChange={updateChange} className="border border-black rounded-lg py-3 px-3 mt-2 bg-white placeholder-white-500 text-black" />
                                                    <label htmlFor='businessEmail_input' className="font-medium text-lg text-black" >Business Email:</label>
                                                    <input id="businessEmail_input" name='businessEmail' type="text" placeholder="Business Email" value={updateForm.businessEmail || ''} onChange={updateChange} className="border border-black rounded-lg py-3 px-3 mt-2 bg-white placeholder-white-500 text-black" />
                                                    <label htmlFor='businessAddress_input' className="font-medium text-lg text-black" >Business Address:</label>
                                                    <input id="businessAddress_input" name='businessAddress' type="text" placeholder="Business Address" value={updateForm.businessAddress || ''} onChange={updateChange} className="border border-black rounded-lg py-3 px-3 mt-2 bg-white placeholder-white-500 text-black" />
                                                </>
                                            )}
                                            <label htmlFor='Phone_input' className="font-medium text-lg text-black " >Phone Number:</label>
                                            <input id="Phone_input" name='phone' type="tel" value={updateForm.phone || ''} onChange={updateChange} placeholder="Phone Number" className="border border-black rounded-lg py-3 px-3 mt-2 bg-white placeholder-white-500 text-black" />
                                            <label htmlFor="countries" className="font-medium text-lg text-black">Country</label>
                                            <select id="countries" name="country" value={updateForm.country || ''} onChange={updateChange} className="bg-gray-50 border border-black text-gray-900 text-sm rounded-lg block py-3 px-3 placeholder-gray-400" >
                                                <option value="" disabled>Choose a country</option>
                                                {
                                                    country.map((item, index) => (
                                                        <option key={index} value={item.country}> {item.country} </option>
                                                    ))
                                                }
                                            </select>
                                            <div className='pt-4'>
                                                <button type='submit' className="border h-full w-full  bg-black text-white rounded-lg py-3 font-semibold text-lg" >Update Account</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <Toastify />
        </>
    )
}

export default PersonalInformation