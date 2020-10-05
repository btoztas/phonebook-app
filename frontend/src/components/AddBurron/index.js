import React from "react";
import { useHistory } from "react-router-dom";
import { Button } from "@material-ui/core";

export default function AddButton() {
  const history = useHistory();

  function handleAdd(contactId) {
    history.push(`/new`);
  }

  return (
    <Button variant="contained" color="primary" onClick={() => handleAdd()}>
      Add Contact
    </Button>
  );
}
