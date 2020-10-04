import React from "react";

import { TextField } from "@material-ui/core";

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
