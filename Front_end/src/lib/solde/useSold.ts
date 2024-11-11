import { useEffect, useState } from "react";
import urls from "../../services/urls";
import { useAuth } from "..";

const useSold = () => {
  const [sold, setSold] = useState();
  const { user } = useAuth();
  const [isDataFetched, setIsDataFetched] = useState(false);

  useEffect(() => {
    // Check if the user ID exists and data hasn't been fetched yet
    if (user?.id && !isDataFetched) {
      // Define an asynchronous function to fetch user data
      const fetchUserData = async () => {
        try {
          // Fetch user data using the provided URL
          const response = await urls.getSellerById(user.id);
          const userData = response.data;
          // Update the 'sold' state with the fetched data
          setSold(userData.sold);
          // Mark data as fetched to prevent re-fetching
          setIsDataFetched(true);
        } catch (error) {
          // Log any errors that occur during data fetching
          console.error("Error fetching user data:", error);
        }
      };

      // Call the fetch function if the user ID is available
      if (user?.id) {
        fetchUserData();
      }
    }
  }, [user?.id, user?.role, isDataFetched]);

  return {
    sold,
    setSold,
  };
};

export default useSold;
