import React from "react";
import { Grid, Typography } from "@material-ui/core";
import ContactForm from "../../components/ContactForm";

export default function EditContact() {
  return (
    <Grid>
      <Typography>Edit Your Contact</Typography>
      <ContactForm></ContactForm>
    </Grid>
  );
}
