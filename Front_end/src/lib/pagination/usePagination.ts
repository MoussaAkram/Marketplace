import { useMemo, useState } from "react";

// Define the usePagination custom hook with a generic type T for items
const usePagination = <T>(items: T[]) => {
  const [currentPage, setCurrentPage] = useState(1); // State to keep track of the current page
  const pageSize = 10; // Define the number of items per page

  // Memoized calculation of paginated data
  const paginatedData = useMemo(() => {
    const startIndex = (currentPage - 1) * pageSize; // Calculate the start index for slicing the array
    return items.slice(startIndex, startIndex + pageSize); // Return the sliced array of items for the current page
  }, [items, currentPage, pageSize]); // Recalculate when items, currentPage, or pageSize change

  // Calculate the total number of pages
  const pageCount = Math.ceil(items.length / pageSize);

  // Return an object with pagination-related data and functions
  return {
    currentPage, // Current page number
    paginatedData, // Array of items for the current page
    setCurrentPage, // Function to update the current page
    pageCount, // Total number of pages
    pageSize, // Number of items per page
  };
};

export default usePagination;
