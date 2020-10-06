import React from "react";
import { Grid, Typography } from "@material-ui/core";
import ContactSearch from "../../components/ContactSearch";
import AddButton from "../../components/AddBurron/index";

/**
 * The Home Page
 */
export default function Home() {
  return (
    <Grid container>
      <Grid container justify="center" xs="12">
        <Typography variant="h3">Search or Add Contacts</Typography>
      </Grid>
      <Grid container justify="center" xs="12">
        <AddButton />
      </Grid>
      <Grid container justify="center" xs="12">
        <ContactSearch />
      </Grid>
    </Grid>
  );
}
