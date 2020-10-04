import React from "react";
import { Grid, Typography } from "@material-ui/core";
import ContactSearch from "../../components/ContactSearch";

export default function Home() {
  return (
    <Grid>
      <Typography>Search Your Contacts</Typography>
      <ContactSearch />
    </Grid>
  );
}
