import React from "react";
import { Grid, Typography } from "@material-ui/core";
import ContactForm from "../../components/ContactForm";

export default function NewContact() {
  return (
    <Grid>
      <Typography>Add a New Contact</Typography>
      <ContactForm />
    </Grid>
  );
}
