import { useState, useEffect } from "react";
import { searchContactRequest } from "./Http/searchContactRequest";

export const useContactSearch = (searchToken) => {
  const [data, setData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (searchToken != null && searchToken !== "")
      searchContactRequest(searchToken)
        .then(({ data }) => {
          setError(null);
          setData(data);
          setIsLoading(false);
        })
        .catch(() => {
          setError(`Something went wrong: ${error}`);
          setIsLoading(false);
        });
  }, [error, searchToken]);

  return [isLoading, data, error];
};
