import { useState, useEffect } from "react";
import { newContactRequest } from "./Http/newContactRequest";

//TODO: Error handling on all these requests
export const useNewContact = (contactData) => {
  const [data, setData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (contactData != null)
      newContactRequest(contactData)
        .then(({ data }) => {
          setError(null);
          setData(data);
          setIsLoading(false);
        })
        .catch((error) => {
          setError(`Something went wrong: ${error}`);
          setIsLoading(false);
        });
  }, [contactData]);

  return [isLoading, data, error];
};
