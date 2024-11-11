import React, { useEffect } from 'react';
import PhotoScroll from './PhotoScroll';
import { Product } from "../../interface/interface.ts";
import urls from "../../services/urls.ts";

const CarouselProduct = ({ currentCategory }) => {
    const [products, setProducts] = React.useState<Product[]>([]);

    // useEffect hook to fetch products when the current category changes
    useEffect(() => {
        // Define an asynchronous function to get products by category
        const getProducts = async () => {
            try {
                // Make an API request to get products for the current category
                const response = await urls.getProductsByCategory(currentCategory);
                // Update the products state with the fetched data
                setProducts(response.data);
            } catch (error) {
                // Log an error message if the request fails
                console.error("Error getProducts", error);
            }
        }

        getProducts(); // Call the getProducts function to initiate the fetch
    }, [currentCategory]); // Dependency array, runs the effect whenever currentCategory changes

    // Extract image data from the products array
    const imageData = products.map(item => item.image);

    // Extract product IDs from the products array
    const hrefs = products.map(item => item.idProduct);

    return (
        <div className='h-[530px]'>
            <h1 className='text-2xl text-center'>You May Also Like</h1>
            <div className="w-full top-0 h-full overflow-hidden">
                <PhotoScroll imageData={imageData} href={hrefs} />
            </div>
        </div>
    );
}

export default CarouselProduct;
