import React, { useEffect, useState } from 'react'
import Funds from './Funds';
import { Pagination, useAuth, usePagination, useSold } from '../../lib';
import urls from '../../services/urls';
import { SellerTransaction } from '../../interface/interface';


const parseDate = (dateStr) => {
    const [datePart, timePart] = dateStr.split(' ');
    const [day, month, year] = datePart.split('/');
    const [hours, minutes] = timePart.split(':');

    return new Date(`${year}-${month}-${day}T${hours}:${minutes}:00`);
};

const Transaction = ({ setShowDetails }) => {

    const [showPrice, setshowPrice] = useState(false);
    const [process, setProcess] = useState('process');
    const [data, setData] = useState<SellerTransaction[]>([]);
    const { user } = useAuth();
    const { currentPage, paginatedData, setCurrentPage, pageSize } = usePagination(data);
    const { sold, setSold } = useSold();

    // Function to handle button clicks and set the current page in state
    const handleButtonClick = (page) => {
        setShowDetails(page); // Update the showDetails state with the selected page
    };

    // Function to toggle the visibility of the price model and set the process state
    function showPriceModel() {
        setshowPrice(!showPrice); // Toggle the showPrice state
        setProcess('process'); // Set the process state to 'process'
    }

    // Effect to fetch seller transaction data based on the user's ID
    useEffect(() => {
        // Asynchronous function to fetch data
        const fetchData = async () => {
            try {
                // Fetch seller transaction data from the API using the user's ID
                const response = await urls.getSellerTransaction(user?.id);
                // Sort the transaction data by payment date in descending order
                const sortedData = response.data.sort((a, b) => {
                    const dateA = parseDate(a.paymentDate); // Parse the payment date for transaction a
                    const dateB = parseDate(b.paymentDate); // Parse the payment date for transaction b
                    return dateB.getTime() - dateA.getTime(); // Compare timestamps for sorting
                });
                setData(sortedData); // Set the sorted data in state
            } catch (error) {
                console.error('Error fetching user data:', error); // Log error if fetching fails
            }
        };

        fetchData(); // Call the fetchData function to retrieve transaction data
    }, [user?.id]);


    return (
        <>
            <header className="border-gray-200 bg-zinc-300">
                <div className="flex flex-wrap justify-between items-center mx-auto max-w-screen-xl p-2">
                    <div className='flex flex-wrap justify-between items-center space-x-24 text-gray-800'>
                        <button onClick={() => handleButtonClick('store')} className="font-manrope font-bold text-xl leading-10 ">Store</button>
                        <button onClick={() => handleButtonClick('addTostore')} className="font-manrope font-bold text-xl leading-10">Add Product</button>
                        <button onClick={() => handleButtonClick('transaction')} className="font-manrope font-bold text-xl leading-10 ">Transaction</button>
                        <button onClick={() => handleButtonClick('vending')} className="font-manrope font-bold text-xl leading-10">Vending</button>
                        <button onClick={showPriceModel} className="font-manrope font-bold text-xl leading-10">Funds</button>
                    </div>
                </div>
            </header>
            <div className='relative min-h-screen  grid'>
                <div className="overflow-auto sm:rounded-lg mr-8  ml-16">
                    <div className='flex  justify-between'>
                        <div className='relative w-full max-w-96 mt-3 mb-5 pb-10 pt-10'>
                            <h1 className='font-semibold text-3xl'>Your Transaction</h1>
                        </div>
                    </div>
                    <table className="w-full text-sm text-left text-gray-800  md:max-lg:w-[769px] max-[600px]:w-[769px]">
                        <thead className="text-xs text-black uppercase bg-cyan-500 ">
                            <tr>
                                <th scope="col" className="px-6 py-3">
                                    Transaction
                                </th>
                                <th scope="col" className="px-6 py-3">
                                    Date
                                </th>
                                <th scope="col" className="px-6 py-3">
                                    Amount
                                </th>
                                <th scope="col" className="px-6 py-3">
                                    Status
                                </th>
                            </tr>
                        </thead>
                        {paginatedData.map((item, index) => (
                            <tbody key={index}>
                                <tr className="bg-white border-b">
                                    <th scope="row" className="px-6 py-4 font-medium whitespace-nowrap">
                                        Take out
                                    </th>
                                    <td className="px-6 py-4">
                                        {item.paymentDate}
                                    </td>
                                    <td className="px-6 py-4">
                                        ${item.amount}
                                    </td>
                                    <td className={`px-6 py-4 font-semibold ${item.paymentStatus === "Completed" ? 'text-green-700' : 'text-red-500'}`}>
                                        {item.paymentStatus}
                                    </td>
                                </tr>
                            </tbody>
                        ))}

                    </table>
                    {data.length > 10 && (
                        <Pagination<SellerTransaction>
                            items={data}
                            pageSize={pageSize}
                            currentPage={currentPage}
                            onPageChange={setCurrentPage}
                        />
                    )}
                </div>
                <Funds setSold={setSold} sold={sold} showPrice={showPrice} togglePriceModel={showPriceModel} process={process} setProcess={setProcess} />
            </div>
        </>
    )
}

export default Transaction