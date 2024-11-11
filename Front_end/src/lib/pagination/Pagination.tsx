import React, { useMemo } from 'react';



interface PaginationProps<T> {
  items: T[];
  pageSize: number;
  currentPage: number;
  onPageChange: (page: number) => void;
}


// Define the Pagination component with generic type T and props
const Pagination = <T,>({ items, pageSize, currentPage, onPageChange }: PaginationProps<T>) => {

  // Calculate the total number of pages based on the items length and page size
  const totalPages = useMemo(() => Math.ceil(items.length / pageSize), [items.length, pageSize]);

  // Function to navigate to the previous page
  const previousPage = () => {
    // Check if the current page is greater than 1
    if (currentPage > 1) {
      onPageChange(currentPage - 1); // Trigger the onPageChange callback with the previous page number
    }
  };

  // Function to navigate to the next page
  const nextPage = () => {
    // Check if the current page is less than the total number of pages
    if (currentPage < totalPages) {
      onPageChange(currentPage + 1); // Trigger the onPageChange callback with the next page number
    }
  };

  return (
    <div className="mt-4 flex justify-center mb-10">
      <button
        className="px-4 py-2 border border-black text-black hover:text-white hover:bg-black"
        onClick={previousPage}
        disabled={currentPage === 1}
      >
        Previous
      </button>
      <span className="px-4 py-2 border-black text-black">{currentPage}</span>
      <button
        className="px-8 py-2 border border-black text-black hover:text-white hover:bg-black"
        onClick={nextPage}
        disabled={currentPage === totalPages}
      >
        Next
      </button>
    </div>
  );
};

export default Pagination;