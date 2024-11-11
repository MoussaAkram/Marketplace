import React, { useEffect, useState } from 'react';
import { Notify, ErrorNotify, RateUI, RateReview, Toastify, useAuth } from '../../lib';
import { Review, ReviewRequist } from "../../interface/interface";
import urls from "../../services/urls";

const Reviews = ({ idproduct }: { idproduct: number | undefined }) => {
    const [reviews, setReviews] = useState<Review[]>([]);
    const [isWritingReview, setIsWritingReview] = useState(false);
    const [comment, setComment] = useState<string>('');
    const [rating, setRating] = useState<number | null>(null);
    const { user, isLoggedIn } = useAuth();

    // useEffect to fetch reviews when the `idproduct` changes.
    useEffect(() => {
        // Check if `idproduct` exists before fetching reviews.
        if (idproduct) {
            fetchReviews(); // Call the function to fetch reviews for the product.
        }
    }, [idproduct]); // Dependency array: This useEffect runs whenever `idproduct` changes.

    // Function to fetch reviews for the given product.
    const fetchReviews = async () => {
        try {
            // API call to fetch reviews based on the product ID.
            const response = await urls.getReviews(idproduct);
            setReviews(response.data); // Update the reviews state with the fetched data.
            console.log(response.data); // Log the reviews data to the console.
        } catch (error) {
            // Log any error encountered during the fetch process.
            console.error("Failed to fetch reviews", error);
        }
    };

    // Function to handle the change in rating selection.
    const handleRatingChange = (selectedRating: number) => {
        setRating(selectedRating); // Update the rating state with the selected rating.
    };

    // Function to handle the change in comment input.
    const handleCommentChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setComment(e.target.value); // Update the comment state with the input value.
    };

    // Function to post a new review.
    const postReview = async (e: React.FormEvent) => {
        e.preventDefault(); // Prevent the default form submission behavior.

        // Check if the user is logged in.
        if (!isLoggedIn) {
            return ErrorNotify('Please log in first'); // Notify the user to log in.
        }

        // Validate if both a comment and a rating have been provided.
        if (!comment || rating === null) {
            return ErrorNotify('Please enter a comment, and select a rating'); // Notify the user of missing input.
        }

        // Create a review object to send to the API.
        const review: ReviewRequist = {
            iduser: parseInt(user.id), // Convert the user ID to an integer.
            idproduct, // Product ID.
            date: new Date().toLocaleString(), // Current date and time.
            rating, // Selected rating.
            comment, // User's comment.
        };

        try {
            // API call to post the review.
            await urls.addReview(review);
            Notify('Review created successfully'); // Notify the user of success.
            fetchReviews(); // Fetch the updated reviews after posting.
            resetForm(); // Reset the form fields after posting.
        } catch (error) {
            // Notify the user of a failure in posting the review.
            ErrorNotify('Failed to create review');
            console.error("Error posting review", error); // Log the error for debugging.
        }
    };

    // Function to reset the form fields (comment and rating) after submission.
    const resetForm = () => {
        setComment(''); // Clear the comment field.
        setRating(null); // Reset the rating selection.
    };

    return (
        <>
            <div className='mb-40'>
                <h1 className='text-2xl text-center'>Review Of Product</h1>
                <div className='flex flex-1 w-full'>
                    <div className="grid h-full w-[600px] mt-20 overflow-hidden">
                        <button
                            className={`mr-4 px-4 py-2 ${!isWritingReview ? 'text-black' : 'text-gray-700'}`}
                            onClick={() => setIsWritingReview(false)}
                        >
                            Show All Reviews
                        </button>
                        <button
                            className={`mr-4 px-4 py-2 ${isWritingReview ? 'text-black' : 'text-gray-700'}`}
                            onClick={() => setIsWritingReview(true)}
                        >
                            Write a Review
                        </button>
                    </div>
                    <div className='w-full mt-20'>
                        {isWritingReview ? (
                            <div className="w-full p-4">
                                <form onSubmit={postReview}>
                                    <div className='mb-4'>
                                        <label htmlFor="rating" className="block mb-2 text-lg font-medium text-gray-900">Rating:</label>
                                        <RateReview onRatingChange={handleRatingChange} />
                                    </div>
                                    <label htmlFor="message" className="block mb-2 text-lg font-medium text-gray-900">Comment:</label>
                                    <textarea
                                        value={comment}
                                        onChange={handleCommentChange}
                                        rows={5}
                                        placeholder="Write your comment here"
                                        className="border rounded p-2 w-full"
                                    />
                                    <button
                                        type='submit'
                                        className="mt-4 ml-4 px-4 py-2 bg-[#052E16] text-white rounded-xl"
                                    >
                                        Submit Review
                                    </button>

                                </form>
                            </div>
                        ) : (
                            <div className="w-full p-4">
                                <h2 className='text-xl mb-4'>All Reviews</h2>
                                {reviews.length > 0 ? (
                                    reviews.map((review) => (
                                        <div key={review.id} className="block max-w-3xl p-6 m-2 bg-white border border-gray-200 rounded-lg shadow">
                                            <div className='flex justify-between'>
                                                <h5 className="mb-2 text-xl font-bold tracking-tight text-gray-900">
                                                    {review.user?.fullName || 'Anonymous'}
                                                </h5>
                                                <h5 className="mb-2 text-base font-medium tracking-tight text-gray-900">
                                                    {review.date}
                                                </h5>
                                            </div>
                                            <p className="font-normal text-gray-700 mb-4 ml-2">{review.comment}</p>
                                            <RateUI rate={review.rating} uniqueId={review.id?.toString() ?? 'unknown'} />
                                        </div>
                                    ))
                                ) : (
                                    <p>No reviews yet.</p>
                                )}
                            </div>
                        )}
                    </div>
                </div>
            </div>
            <Toastify />
        </>
    );
};

export default Reviews;
