import React from "react";

import { TextField } from "@material-ui/core";

/**
 * The Contact Search Input component is responsible to receive a String input,
 * use the Search Contact API and call the passed function #onInputChange
 * passing as an argument the results given by the API.
 */
export default function ContactSearchInput({ onInputChange }) {
  return (
    <form noValidate autoComplete="off">
      <TextField
        id="token"
        label="Search for Contact"
        variant="outlined"
        onChange={(event) => onInputChange(event.target.value)}
      />
    </form>
  );
}
