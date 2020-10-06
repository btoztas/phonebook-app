import { useState, useEffect } from "react";
import { getContactRequest } from "./Http/getContactRequest";

//TODO: Error handling on all these requests
/**
 * The Hook responsible to Edit a Contact via HTTP.
 */
export const useGetContact = (contactId) => {
  const [data, setData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (contactId != null)
      getContactRequest(contactId)
        .then(({ data }) => {
          setError(null);
          setData(data);
          setIsLoading(false);
        })
        .catch((error) => {
          setError(`Something went wrong: ${error}`);
          setIsLoading(false);
        });
  }, [contactId]);

  return [isLoading, data, error];
};
