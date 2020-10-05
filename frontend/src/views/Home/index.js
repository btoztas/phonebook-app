import React from "react";
import { Grid, Typography } from "@material-ui/core";
import ContactSearchAndEdit from "../../components/ContactSearch";
import AddButton from "../../components/AddBurron/index";

export default function Home() {
  return (
    <Grid justify="center" xs="12">
      <Grid justify="center" xs="6">
        <Typography variant="h3">Search Your Contacts</Typography>
      </Grid>
      <Grid justify="center" xs="6">
        <AddButton />
      </Grid>
      <Grid justify="center" xs="12">
        <ContactSearchAndEdit />
      </Grid>
    </Grid>
  );
}
