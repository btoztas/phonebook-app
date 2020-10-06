import React from "react";
import AddButton from "../../components/AddBurron";
import HomeButton from "../../components/HomeButton/index";

import { Grid, Typography } from "@material-ui/core";

/**
 * The Page Not Found Page
 */
export default function PageNotFound() {
  return (
    <Grid>
      <Grid>
        <Typography>Page Not Found...</Typography>
      </Grid>
      <Grid>
        <HomeButton />
        <AddButton />
      </Grid>
    </Grid>
  );
}
