import React from "react";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
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
