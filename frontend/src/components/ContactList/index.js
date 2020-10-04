import React from "react";

import {
  Typography,
  Table,
  TableHead,
  TableBody,
  TableFooter,
} from "@material-ui/core";
import ConctactListEntry from "../ContactListEntry";

export default function ContactList({ contacts }) {
  if (contacts.length === 0) return <Typography>No Results Found</Typography>;

  return (
    <Table>
      <TableHead />
      <TableBody>
        {contacts.map((contactItem) => (
          <ConctactListEntry contactData={contactItem} />
        ))}
      </TableBody>
      <TableFooter />
    </Table>
  );
}
