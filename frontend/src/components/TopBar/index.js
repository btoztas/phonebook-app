import React from "react";
import { AppBar, Toolbar, Typography } from "@material-ui/core";
import ContactPhoneIcon from "@material-ui/icons/ContactPhone";

export default function TopBar() {
  return (
    <AppBar position="static">
      <Toolbar>
        <ContactPhoneIcon />
        <Typography variant="h6"> Phone Book</Typography>
      </Toolbar>
    </AppBar>
  );
}
