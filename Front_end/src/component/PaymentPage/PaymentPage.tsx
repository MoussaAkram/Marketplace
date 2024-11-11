import React, { useEffect, useState } from 'react'
import { PayButton, SectionWrapper } from '../../lib'
import { Link, useParams } from 'react-router-dom';
import urls from '../../services/urls';
import { Ordre, OrdreforPayment, Productqte } from "../../interface/interface.ts";


interface PaymentPageProps {
    onPaymentComplete: (status: 'success' | 'failed') => void;
}

const PaymentPage: React.FC<PaymentPageProps> = ({ onPaymentComplete }) => {
    const { ordreid } = useParams<{ ordreid: string }>();
    const [ordre, setOrdre] = useState<Ordre>({});

    // useEffect hook to fetch order details when `ordreid` changes.
    useEffect(() => {
        // Check if `ordreid` exists before making the request.
        if (ordreid) {
            // Async function to fetch the order data by its ID.
            const getOrdre = async () => {
                try {
                    // Call the API to get order details using the `ordreid`.
                    const response = await urls.getOrdreById(parseInt(ordreid));
                    setOrdre(response.data);  // Store the fetched order data in the state.
                } catch (error) {
                    // Log any error encountered during the fetching process.
                    console.error("Error fetching order:", error);
                }
            };
            getOrdre();  // Invoke the function to fetch order data.
        }
    }, [ordreid]);  // Dependency array: This useEffect runs when `ordreid` changes.

    // Function to calculate the subtotal for a product item (price * quantity).
    const calculateSubtotal = (item: Productqte) => {
        // Get the quantity of the product; default to 0 if `qte` is not defined.
        const quantity = item.qte || 0;
        // Get the price of the product; default to 0 if `price` is not defined.
        const price = item.product?.price || 0;
        // Calculate subtotal by multiplying quantity and price, then format it to 2 decimal places.
        return (quantity * price).toFixed(2);
    };

    // Function to return the total amount for the order.
    const total = () => {
        return ordre.amount;  // The total amount is stored in the `ordre.amount` field.
    };

    // Function to handle the purchase process once it's completed.
    const handlePurchaseComplete = async (order: OrdreforPayment) => {
        // Convert `ordreid` to a number.
        const ordreidNumber = Number(ordreid);

        // Check if the `ordreidNumber` is a valid number.
        if (isNaN(ordreidNumber)) {
            console.error('Invalid order ID, not a number.');
            return;  // Exit the function if the order ID is invalid.
        }

        // Create the `order` object, ensuring the correct structure is passed for purchase.
        order = {
            idOrdre: ordreidNumber,  // Order ID.
            idUser: parseInt(ordre.user?.id),  // User ID, convert to integer.
            amount: ordre.amount,  // The total amount for the order.
            productqtes: ordre.productqtes,  // The list of products and quantities in the order.
        };

        try {
            // Call the API to complete the purchase.
            const { data } = await urls.buyProduct(order);

            // Check if the purchase was successful.
            if (data.success) {
                // Store the payment status in the session storage.
                sessionStorage.setItem('paymentStatus', 'success');
                // Call the `onPaymentComplete` function with a success status.
                onPaymentComplete('success');
                // Redirect the user to the success URL provided by the API.
                window.location.href = `${data.redirectUrl}`;
            } else {
                // Call the `onPaymentComplete` function with a failure status.
                onPaymentComplete('failed');
                // Redirect the user to the failure URL provided by the API.
                window.location.href = `${data.redirectUrl}`;
            }
        } catch (error) {
            const { data } = await urls.buyProduct(order);
            // If there's an error in the purchase process, log the error.
            console.error('Error:', error);

            // Call the `onPaymentComplete` function with a failure status.
            onPaymentComplete('failed');

            // Redirect the user to the failure URL provided by the API.
            window.location.href = `${data.redirectUrl}`;
        }
    };

    return (
        <>
            <div
                className="bg-gray-50 relative z-10 after:contents-[''] after:absolute after:z-0 after:h-full xl:after:w-1/3 after:top-0 after:right-0 after:bg-gray-50">
                <div className="w-full max-w-6xl px-4 md:px-5 lg-6 mx-auto relative z-10">
                    <div className="">
                        <div className="col-span-12 xl:col-span-8 lg:pr-8 pt-14 pb-8 lg:py-24 w-full max-xl:max-w-3xl max-xl:mx-auto">
                            <div className="flex items-center justify-between pb-8 border-b border-gray-300">
                                <h2 className="font-manrope font-bold text-3xl leading-10 text-black">Checkout</h2>
                                <h2 className="font-manrope font-bold text-xl leading-8 text-gray-600">{ordre.productqtes?.length} Items</h2>
                            </div>
                            <div className="grid grid-cols-12 mt-8 max-md:hidden pb-6 border-b border-gray-200">
                                <div className="col-span-12 md:col-span-7">
                                    <p className="font-normal text-lg leading-8 text-gray-400">Product Details</p>
                                </div>
                                <div className="col-span-12 md:col-span-5">
                                    <div className="grid grid-cols-5">
                                        <div className="col-span-3">
                                            <p className="font-normal text-lg leading-8 text-gray-400 text-center">Quantity</p>
                                        </div>
                                        <div className="col-span-2">
                                            <p className="font-normal text-lg leading-8 text-gray-400 text-center ml-24">Subtotal</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            {ordre.productqtes?.map((item, index) => (
                                <div key={index} className="flex flex-col min-[500px]:flex-row min-[500px]:items-center gap-5 py-6  border-b border-gray-200 group">
                                    <div className="w-full md:max-w-[126px]">
                                        <img src={item.product?.image} alt="...." className="mx-auto rounded-xl" />
                                    </div>
                                    <div className="grid grid-cols-1 md:grid-cols-4 w-full">
                                        <div className="md:col-span-2">
                                            <div className="flex flex-col max-[500px]:items-center gap-3">
                                                <h6 className="font-semibold text-base leading-7 text-black mb-8"> {item.product?.name} </h6>
                                                <h6 className="font-medium text-base mb-2 leading-7 text-gray-600 transition-all duration-300 group-hover:text-indigo-600"> ${item.product?.price} </h6>
                                            </div>
                                        </div>
                                        <div className="flex items-center max-[500px]:justify-center md:justify-end h-full max-md:mt-3">
                                            <div className="flex items-center justify-around w-24 h-8">
                                                <h1 className="focus:outline-none text-center w-full font-semibold text-md hover:text-black focus:text-black  md:text-basecursor-default flex items-center text-gray-700  outline-none" > X{item.qte} </h1>
                                            </div>
                                        </div>
                                        <div className="flex items-center max-[500px]:justify-center md:justify-end max-md:mt-3 h-full">
                                            <p className="font-bold text-lg leading-8 text-gray-600 text-center transition-all duration-300 group-hover:text-indigo-600"> ${calculateSubtotal(item)} </p>
                                        </div>
                                        <div>

                                        </div>

                                    </div>
                                </div>
                            ))
                            }
                        </div>
                    </div>
                    <div className='flex justify-center pb-20'>
                        <div className="flex items-center justify-between max-w-5xl w-full h-full p-6 bg-[#D9D9D9] border border-gray-200 rounded-lg shadow">
                            <h5 className="text-2xl font-bold tracking-tight text-gray-900">Order Total:</h5>
                            <h1 className="text-xl font-bold tracking-tight text-gray-900">${total()}</h1>
                        </div>
                    </div>
                    <div className='pb-44'>
                        <div>
                            <div className="mx-auto px-4 2xl:px-0">
                                <div className="mx-auto ">
                                    <div className="sm:mt-8 lg:flex lg:items-start lg:gap-12">
                                        <form action="#" className="w-full p-4 sm:p-6">
                                            <div className='flex justify-center items-center gap-8 '>
                                                <Link to={'/cart'} className="flex items-center justify-center rounded-md border border-black  px-10 py-2 text-sm font-medium text-black shadow-lg hover:bg-black hover:text-white">Go to Cart</Link>
                                                <PayButton
                                                    item={ordre}
                                                    handlePurchaseComplete={handlePurchaseComplete}
                                                />

                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </>
    )
}

export default SectionWrapper(PaymentPage);



