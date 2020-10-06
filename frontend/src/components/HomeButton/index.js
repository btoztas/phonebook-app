import React from "react";
import { useHistory } from "react-router-dom";
import HomeIcon from "@material-ui/icons/Home";
import { Button, IconButton } from "@material-ui/core";

export default function HomeButton() {
  const history = useHistory();

  function handleHome() {
    history.push(`/`);
  }

  return (
    <Button onClick={() => handleHome()}>
      <IconButton color="primary">
        <HomeIcon />
      </IconButton>
    </Button>
  );
}
