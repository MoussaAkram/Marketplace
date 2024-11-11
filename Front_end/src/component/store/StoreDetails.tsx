import React, { useEffect, useState } from 'react'
import { RateAd, useSold } from '../../lib';
import Funds from './Funds';
import { Category, Product } from "../../interface/interface.ts";
import urls from "../../services/urls.ts";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";


const StoreDetails = ({ setShowDetails, productid }) => {
    const [categorys, setCategory] = useState<Category[]>([]);
    const [image, setImage] = useState<File | null>(null);
    const [name, setName] = useState('');
    const [id, setid] = useState<number>(0);
    const [description, setdescriptionName] = useState('');
    const [price, setprice] = useState<number>(1);
    const [qte, setqte] = useState<number>(1);

    const { sold, setSold } = useSold();
    const [deleteModalOpen, setDeleteModalOpen] = useState(false);
    const [updateModalOpen, setUpdateModalOpen] = useState(false);
    const [showPrice, setshowPrice] = useState(false);
    const [process, setProcess] = useState('process');
    const [product, setproduct] = useState<Product | undefined>(undefined);


    // Function to fetch a specific product by its ID
    const getproducts = async () => {
        try {
            // Fetch the product data from the API
            const resp = await urls.getOneProduct(productid);
            setproduct(resp.data); // Set the fetched product data in state
        } catch (e) {
            console.log("Error getproducts", e); // Log error if fetching fails
        }
    };

    // Effect to fetch the product whenever the product ID changes
    useEffect(() => {
        getproducts(); // Call the function to fetch the product
    }, [productid]); // Dependency array to trigger the effect on productid change

    // Function to fetch categories from the API
    const getCategories = async () => {
        try {
            // Fetch categories from the API
            const response = await urls.getCategories();
            setCategory(response.data); // Set the fetched categories in state
        } catch (error) {
            console.error("Failed to load data", error); // Log error if fetching fails
        }
    };

    // Effect to fetch categories when the component mounts
    useEffect(() => {
        getCategories(); // Call the function to fetch categories
    }, []); // Empty dependency array means this effect runs only once

    // Function to toggle the visibility of the delete modal
    function showDeleteModel() {
        setDeleteModalOpen(!deleteModalOpen); // Toggle delete modal state
    }

    // Function to open the update modal and set product details
    function showUpdateModel(product: Product | undefined) {
        setUpdateModalOpen(!updateModalOpen); // Toggle update modal state
        // Set state values based on the selected product details
        setdescriptionName(product?.description || "");
        setprice(product?.price || 0);
        setName(product?.name || "");
        setid(product?.idcategory || 0);
        setqte(product?.stock || 0);
    }

    // Function to handle button clicks and set the current page in state
    const handleButtonClick = (page) => {
        setShowDetails(page); // Update the showDetails state with the selected page
    };

    // Function to toggle the visibility of the price model and set the process state
    function showPriceModel() {
        setshowPrice(!showPrice); // Toggle the showPrice state
        setProcess('process'); // Set the process state to 'process'
    }

    // Function to delete a product by its ID
    const handledeleteproduct = async () => {
        try {
            await urls.deleteProduct(productid); // Call API to delete the product
            setDeleteModalOpen(false); // Close the delete modal
            handleButtonClick('store'); // Redirect to the store page
        } catch (e) {
            console.log("Error delete product", e); // Log error if deletion fails
        }
    };

    // Function to handle image selection from an input
    const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        // Check if files are selected
        if (e.target.files && e.target.files.length > 0) {
            setImage(e.target.files[0]); // Set the selected image in state
        }
    };

    // Function to handle name input changes
    const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setName(e.target.value); // Update the name state with the input value
    };

    // Function to handle description input changes
    const handlesetdescriptionNameChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setdescriptionName(e.target.value); // Update the description state with the input value
    };

    // Function to handle price input changes and validate input
    const handlePriceChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const inputValue = e.target.value;

        // Regular expression to allow only numbers with a period for up to 2 decimal places
        if (inputValue === '' || /^\d+(\.\d{0,2})?$/.test(inputValue)) {
            setprice(inputValue); // Update price state with valid input
        }
    };

    // Function to prevent typing of decimal points in inputs
    const handleKeyDown = (e) => {
        // Prevent typing decimal points (period and comma)
        if (e.key === '.' || e.key === ',') {
            e.preventDefault(); // Prevent default behavior if decimal point is typed
        }
    };

    // Function to handle quantity input changes and validate input
    const handleQteChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const inputValue = e.target.value;

        // Allow only empty input or numeric values
        if (inputValue === '' || /^\d*$/.test(inputValue)) {
            setqte(inputValue); // Update quantity state with valid input
        }
    };

    // Function to handle form submission
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault(); // Prevent default form submission
        handleupdateProduct(); // Call function to update the product
    };

    // Function to update a product's details
    const handleupdateProduct = async () => {
        const formData = new FormData(); // Create a FormData object for the product update
        const currentDate: Date = new Date(); // Get the current date

        // Append product data to the FormData object
        formData.append('productData', JSON.stringify({
            idcategory: id,
            name: name,
            stock: qte,
            description: description,
            price: price,
            daTe_creation: currentDate.toLocaleString() // Format date for submission
        }));

        // If an image is selected, append it to the FormData
        if (image) {
            formData.append('file', image);
        }

        try {
            // Call the API to update the product with the FormData
            await urls.updateProduct(productid, formData);
            // Reset state values after the update
            setName("");
            setImage(null);
            setprice(0);
            setqte(1);
            setid(0);
            setdescriptionName("");
            showUpdateModel(undefined); // Close the update modal
            getproducts(); // Fetch the updated product data
        } catch (error) {
            console.error("Failed to add category", error); // Log error if updating fails
        }
    };


    return (
        <>
            <header className="border-gray-200 bg-zinc-300">
                <div className="flex flex-wrap justify-between items-center mx-auto max-w-screen-xl p-2">
                    <div className='flex flex-wrap justify-between items-center space-x-24 text-gray-800'>
                        <button onClick={() => handleButtonClick('store')}
                            className="font-manrope font-bold text-xl leading-10 ">Store
                        </button>
                        <button onClick={() => handleButtonClick('addTostore')}
                            className="font-manrope font-bold text-xl leading-10">Add Product
                        </button>
                        <button onClick={() => handleButtonClick('transaction')}
                            className="font-manrope font-bold text-xl leading-10 ">Transaction
                        </button>
                        <button onClick={() => handleButtonClick('vending')}
                            className="font-manrope font-bold text-xl leading-10">Vending
                        </button>
                        <button onClick={showPriceModel} className="font-manrope font-bold text-xl leading-10">Funds
                        </button>
                    </div>
                </div>
            </header>
            <div className="container p-2 m-5">
                <button onClick={() => handleButtonClick('store')} className='text-cyan-700 mb-8'>
                    <ArrowBackIosNewIcon className='mb-2 h-3 w-3' /> <span
                        className='font-bold text-2xl ml-1'>Back</span>
                </button>
            </div>
            <div className="flex">
                <div className="h-full"></div>
                <div className='ml-10'>
                    <div className="mb-20">
                        <div className="flex flex-col mb-4">
                            <div
                                className="relative ml-10 flex flex-col md:flex-row md:space-x-5 space-y-3 md:space-y-0 rounded-xl shadow-lg p-3 max-w-6xl mx-auto border border-white bg-white">
                                <div className="w-full md:w-1/4 bg-white grid place-items-center">
                                    <img src={product?.image} alt="...." className="rounded-xl" />
                                </div>
                                <div className="w-full md:w-2/3 bg-white flex flex-col space-y-2 p-3">
                                    <h3 className="font-black text-gray-800 md:text-3xl text-xl">{product?.name}</h3>
                                    <h5 className="">{product?.description}</h5>
                                    <div className="pt-4">
                                        <RateAd idproduct={product?.idProduct} />
                                    </div>
                                    <h5 className="pt-4 pb-4">Price: ${product?.price}</h5>
                                    <div className="flex justify-around items-start w-full pt-4">
                                        <button onClick={() => showUpdateModel(product)}
                                            className="text-white bg-gradient-to-br from-purple-600 to-blue-500 hover:bg-gradient-to-bl focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-md px-7 py-2.5 text-center mb-2 w-5/12">
                                            Update
                                        </button>
                                        <button onClick={showDeleteModel}
                                            className="text-white bg-gradient-to-br from-pink-500 to-orange-400 hover:bg-gradient-to-bl focus:ring-4 focus:outline-none focus:ring-pink-200 font-medium rounded-lg text-md px-7 py-2.5 text-center mb-2 w-5/12">
                                            Delete
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className={` z-20 w-full ${deleteModalOpen ? '' : 'hidden'}`}>
                            <div className="h-screen w-screen bg-black opacity-50 fixed top-0 left-0"></div>
                            <div id="deleteModal" tabIndex={-1} aria-hidden="true"
                                className=" justify-center grid content-center fixed top-0 overflow-y-auto overflow-x-hidden right-0 left-0 z-50  items-center w-full md:inset-0 md:h-full">
                                <div className="relative p-4 w-full max-w-md h-full md:h-auto">
                                    <div className="relative p-4 text-center bg-white rounded-lg shadow sm:p-5">
                                        <button onClick={showDeleteModel} type="button"
                                            className="text-gray-400 absolute top-2.5 right-2.5 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center"
                                            data-modal-toggle="deleteModal">
                                            <svg aria-hidden="true" className="w-5 h-5" fill="currentColor"
                                                viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                                                <path fillRule="evenodd"
                                                    d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                                                    clipRule="evenodd"></path>
                                            </svg>
                                            <span className="sr-only">Close modal</span>
                                        </button>
                                        <svg className="text-gray-400 w-11 h-11 mb-3.5 mx-auto" aria-hidden="true"
                                            fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                                            <path fillRule="evenodd"
                                                d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                                                clipRule="evenodd"></path>
                                        </svg>
                                        <p className="mb-4 text-gray-500">Are you sure you want to delete this
                                            product?</p>
                                        <div className="flex justify-center items-center space-x-4">
                                            <button onClick={showDeleteModel} data-modal-toggle="deleteModal"
                                                type="button"
                                                className="py-2 px-3 text-sm font-medium text-gray-500 bg-white rounded-lg border border-gray-200 hover:bg-gray-100 focus:ring-4 focus:outline-none focus:ring-primary-300 hover:text-gray-900 focus:z-10">
                                                No, cancel
                                            </button>
                                            <button type="submit" onClick={() => handledeleteproduct()}
                                                className="py-2 px-3 text-sm font-medium text-center text-white bg-red-600 rounded-lg hover:bg-red-700 focus:ring-4 focus:outline-none focus:ring-red-300 dark:bg-red-500 dark:hover:bg-red-600 dark:focus:ring-red-900">
                                                Yes, I'm sure
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className={` z-20 w-full ${updateModalOpen ? '' : 'hidden'}`}>
                            <div className="h-screen w-screen bg-black opacity-50 fixed top-0 left-0"></div>
                            <div id="defaultModalUpdate" tabIndex={-1}
                                className="scrollbar-none justify-center  grid content-center  fixed top-0 left-0 right-0 w-full p-4 overflow-x-hidden overflow-y-auto md:inset-0 h-[calc(100%-1rem)] max-h-full">
                                <div className="relative w-full max-w-2xl max-h-screen">
                                    <div
                                        className="w-[800px]  md:max-lg:w-[650px] max-[600px]:w-[300px] relative bg-white rounded-lg shadow">
                                        <div className="flex items-start justify-between p-2 border-b rounded-t">
                                            <h3 className="text-xl font-semibold text-gray-900">
                                                Update Product
                                            </h3>
                                            <button onClick={() => showUpdateModel(product)} type="button"
                                                className="text-gray-400 bg-transparent hover:bg-rose-500  hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center"
                                                data-modal-hide="defaultModal">
                                                <svg className="w-5 h-5" fill="currentColor"
                                                    viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                                                    <path fillRule="evenodd"
                                                        d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                                                        clipRule="evenodd"></path>
                                                </svg>
                                                <span className="sr-only">Close modal</span>
                                            </button>
                                        </div>
                                        <form onSubmit={handleSubmit}>
                                            <div className="relative px-5 z-0 w-full mb-1 group mt-2">
                                                <label htmlFor="name-input-Update"
                                                    className="block text-sm font-medium text-gray-900 ">Name</label>
                                                <input name="name" onChange={handleNameChange} value={name}
                                                    placeholder="Write name..." type="text" id="name-input-Update"
                                                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" />
                                            </div>
                                            <div className="relative px-5 z-0 w-full mb-1 group">
                                                <label htmlFor="price-input-Update"
                                                    className="block  text-sm font-medium text-gray-900 ">Price</label>
                                                <input name="price" onChange={handlePriceChange} value={price}
                                                    placeholder="Write your price here..." type="number"
                                                    id="price-input-Update"
                                                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" />
                                            </div>
                                            <div className="relative px-5 z-0 w-full mb-1 group">
                                                <label htmlFor="quantity-input-Update"
                                                    className="block  text-sm font-medium text-gray-900 ">Quantity</label>
                                                <input name="stock" onChange={handleQteChange} value={qte} onKeyDown={handleKeyDown}
                                                    placeholder="Write your quantity here..." type="number"
                                                    id="quantity-input-Update"
                                                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" />
                                            </div>
                                            <div className="relative px-5 z-0 w-full mb-1 group">
                                                <label htmlFor="category-input-Update"
                                                    className="block  text-sm font-medium text-gray-900 ">Category</label>
                                                <select
                                                    onChange={(e) => setid(Number(e.target.value))} name='idcategory' value={id}
                                                    id="category-input-Update"
                                                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                                                    required>
                                                    <option disabled value={0}>Choose a category</option>
                                                    {categorys.map((item) => (
                                                        <option key={item.id} value={item.id}>{item.name}</option>
                                                    ))}
                                                </select>
                                            </div>
                                            <div className="relative z-0 w-full px-5 mb-1 group">
                                                <label htmlFor="description-input-update"
                                                    className="block  text-sm font-medium text-gray-900">Description</label>
                                                <textarea name="description" value={description}
                                                    onChange={handlesetdescriptionNameChange}
                                                    id="description-input-update" rows={3}
                                                    className="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 focus:outline-none"
                                                    placeholder="Write your thoughts here..."></textarea>
                                            </div>
                                            <div className="relative z-0 w-full px-5 mt-3 group">
                                                <label className="block mb-1 text-sm font-medium text-gray-900 "
                                                    htmlFor="file_update_input">Upload Image</label>
                                                <input onChange={handleImageChange} name="image"
                                                    className="block w-full text-sm text-gray-900 border border-gray-300 rounded-lg cursor-pointer bg-gray-50 focus:outline-none file:bg-slate-900 file:text-white file:border-0 file:me-4 file:py-2 file:px-2"
                                                    aria-describedby="file_update_input" id="file_update_input"
                                                    type="file" />
                                            </div>
                                            <div
                                                className="flex items-center p-6 space-x-2 border-t border-gray-200 rounded-b">
                                                <button
                                                    data-modal-hide="defaultModalUpdate" type="submit"
                                                    className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center">Submit
                                                </button>
                                                <button onClick={() => showUpdateModel(product)} data-modal-hide="defaultModalUpdate"
                                                    type="button"
                                                    className="text-gray-500 bg-white hover:bg-gray-100 focus:ring-4 focus:outline-none focus:ring-blue-300 rounded-lg border border-gray-200 text-sm font-medium px-5 py-2.5 hover:text-gray-900 focus:z-10">Decline
                                                </button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <Funds setSold={setSold} sold={sold} showPrice={showPrice} togglePriceModel={showPriceModel} process={process}
                        setProcess={setProcess} />
                </div>
            </div>
        </>
    )
}

export default StoreDetails