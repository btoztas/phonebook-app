import React from "react";

import {
  Typography,
  TableRow,
  TableCell,
  Button,
  IconButton,
} from "@material-ui/core";
import EditIcon from "@material-ui/icons/Edit";

export default function ConctactListEntry({ contactData }) {
  return (
    <TableRow hover>
      <TableCell>
        <Typography>{contactData.firstName}</Typography>
      </TableCell>
      <TableCell>
        <Typography>{contactData.lastName}</Typography>
      </TableCell>
      <TableCell>
        <Typography>{contactData.phoneNumber}</Typography>
      </TableCell>
      <TableCell>
        <Button>
          <IconButton color="primary">
            <EditIcon />
          </IconButton>
        </Button>
      </TableCell>
    </TableRow>
  );
}
