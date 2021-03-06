import { useState, useEffect } from "react";
import { editContactRequest } from "./Http/editContactRequest";

//TODO: Error handling on all these requests
export const useEditContact = (newContactData) => {
  const [data, setData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (newContactData != null)
      editContactRequest(newContactData)
        .then(({ data }) => {
          setError(null);
          setData(data);
          setIsLoading(false);
        })
        .catch((error) => {
          setError(`Something went wrong: ${error}`);
          setIsLoading(false);
        });
  }, [newContactData]);

  return [isLoading, data, error];
};
