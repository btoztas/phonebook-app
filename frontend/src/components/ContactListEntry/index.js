import React from "react";
import { useHistory } from "react-router-dom";

import {
  Typography,
  TableRow,
  TableCell,
  Button,
  IconButton,
} from "@material-ui/core";
import EditIcon from "@material-ui/icons/Edit";

export default function ConctactListEntry({ contactData }) {
  const history = useHistory();

  function handleEdit(contactId) {
    history.push(`/edit/${contactId}`);
  }

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
        <Button onClick={() => handleEdit(contactData.id)}>
          <IconButton color="primary">
            <EditIcon />
          </IconButton>
        </Button>
      </TableCell>
    </TableRow>
  );
}
