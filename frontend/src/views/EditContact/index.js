import React, { useState } from "react";
import { useParams } from "react-router-dom";
import { Grid, Typography } from "@material-ui/core";
import { useEditContact } from "../../services/ContactsApi/editContact";
import { useGetContact } from "../../services/ContactsApi/getContact";
import ContactForm from "../../components/ContactForm";
import HomeButton from "../../components/HomeButton";
import AddButton from "../../components/AddBurron";

/**
 * The Edit Contact Page
 */
export default function EditContact() {
  const { contactId } = useParams();
  const [formInput, setFormInput] = useState(null);
  const [isFormSubmitted, setIsFormSubmitted] = useState(false);
  const [isPerformingFetch, currentData, errorFetch] = useGetContact(contactId);
  const [isPerformingUpdate, , errorUpdate] = useEditContact(formInput);

  const onFormSubmit = (formData) => {
    setFormInput(formData);
    setIsFormSubmitted(true);
  };

  if (isPerformingFetch)
    return (
      <Grid>
        <Typography>Fetching Contact {contactId} data</Typography>
      </Grid>
    );

  if (errorFetch)
    return (
      <Grid>
        <Typography>
          Something went wrong getting the current data for contact {contactId}:{" "}
          {errorFetch}
        </Typography>
        <HomeButton />
        <AddButton />
      </Grid>
    );
  if (isFormSubmitted && isPerformingUpdate)
    return (
      <Grid>
        <Typography>Submitting update on contact {contactId}...</Typography>
      </Grid>
    );

  if (isFormSubmitted && !isPerformingUpdate)
    if (errorUpdate == null)
      return (
        <Grid>
          <Typography>Contact Updated!</Typography>
          <HomeButton />
          <AddButton />
        </Grid>
      );
    else
      return (
        <Grid>
          <Typography>
            Something went wrong updating contact: {errorUpdate}
          </Typography>
          <HomeButton />
          <AddButton />
        </Grid>
      );

  return (
    <Grid>
      <Typography>Edit Contact</Typography>
      <ContactForm
        defaultContactValue={currentData}
        onFormSubmit={onFormSubmit}
      />
    </Grid>
  );
}
