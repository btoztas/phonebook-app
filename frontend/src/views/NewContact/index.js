import React, { useState } from "react";
import { Grid, Typography } from "@material-ui/core";
import ContactForm from "../../components/ContactForm";
import { useNewContact } from "../../services/ContactsApi/newContact";
import ConctactListEntry from "../../components/ContactListEntry";

import HomeButton from "../../components/HomeButton";
import AddButton from "../../components/AddBurron";

export default function NewContact() {
  const [formInput, setFormInput] = useState(null);
  const [isFormSubmitted, setIsFormSubmitted] = useState(false);
  const [isLoading, data, error] = useNewContact(formInput);

  const onFormSubmit = (formData) => {
    setFormInput(formData);
    setIsFormSubmitted(true);
  };

  if (isFormSubmitted && isLoading)
    return (
      <Grid>
        <Typography>Submitting new contact...</Typography>
      </Grid>
    );

  if (isFormSubmitted && !isLoading)
    if (error == null)
      return (
        <Grid>
          <Typography>New Contact Created!</Typography>
          <ConctactListEntry contactData={data} />
          <HomeButton />
          <AddButton />
        </Grid>
      );
    else
      return (
        <Grid>
          <Typography>
            Something went wrong creating your new contact: {error}
          </Typography>
        </Grid>
      );

  return (
    <Grid>
      <Typography>Add a New Contact</Typography>
      <ContactForm onFormSubmit={onFormSubmit} />
    </Grid>
  );
}
