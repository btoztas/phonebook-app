import React from "react";
import { useHistory } from "react-router-dom";
import { Button } from "@material-ui/core";

/**
 * The Add button that can be used across the application to redirect the user
 * to the Add Contact Page.
 */
export default function AddButton() {
  const history = useHistory();

  function handleAdd() {
    history.push(`/new`);
  }

  return (
    <Button variant="contained" color="primary" onClick={() => handleAdd()}>
      Add Contact
    </Button>
  );
}
