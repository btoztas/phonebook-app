import React from "react";

import {
  Typography,
  Table,
  TableHead,
  TableBody,
  TableFooter,
} from "@material-ui/core";
import ConctactListEntry from "../ContactListEntry";

/**
 * Component responsible to display a List of Contacts passed as argument.
 * Internally uses the ContactListEntry to display each one of the Contacts.
 */
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
