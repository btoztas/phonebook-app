import React, { useState } from "react";

import { Grid, Typography } from "@material-ui/core";
import ContactSearchInput from "../ContactSearchInput";
import ContactList from "../ContactList";
import { useContactSearch } from "../../services/ContactsApi/searchContact";

/**
 * The Contact Search component is responsible to handle the Contact Search.
 */
export default function ContactSearch() {
  const [hasPerformedSearch, setHasPerformedSearch] = useState(false);
  const [searchToken, setSearchToken] = useState(null);
  const [isLoading, data, error] = useContactSearch(searchToken);

  const onSearchInput = (inputSearchToken) => {
    if (inputSearchToken == null || inputSearchToken === "")
      setHasPerformedSearch(false);
    else {
      setSearchToken(inputSearchToken);
      setHasPerformedSearch(true);
    }
  };

  const searchInputGrid = (
    <Grid container justify="center" xs="12">
      <ContactSearchInput onInputChange={onSearchInput} />
    </Grid>
  );

  if (!hasPerformedSearch) return <Grid>{searchInputGrid}</Grid>;
  if (isLoading)
    return (
      <Grid container justify="center">
        {searchInputGrid}
        <Grid>
          <Typography>Loading Search...</Typography>
        </Grid>
      </Grid>
    );

  if (error != null)
    return (
      <Grid>
        {searchInputGrid}
        <Grid>
          <Typography>Error Performing Search</Typography>
        </Grid>
      </Grid>
    );

  if (data == null || data.length === 0)
    return (
      <Grid>
        {searchInputGrid}
        <Grid>
          <Typography>No contacts found</Typography>
        </Grid>
      </Grid>
    );

  return (
    <Grid>
      {searchInputGrid}
      <Grid>
        <ContactList contacts={data} />
      </Grid>
    </Grid>
  );
}
