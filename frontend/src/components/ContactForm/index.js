import React, { useState, useEffect } from "react";

import { TextField, Button } from "@material-ui/core";

const PHONE_NUMBER_REGEX = new RegExp("^[+][0-9]+[ ][0-9]+[ ][0-9]{6}$");

/**
 * Component responsible to handle a contact form. It can be used to handle new
 * Contacts or to edit existing contacts: to display the current contact data,
 * the calling component should pass in the current contact data as the first
 * argument. The second argument is the function that this component should call
 * to handle the change in input of the form - it will pass to this fuction as
 * an argument the most updated state of the form everytime the used changes.
 */
export default function ContactForm({ defaultContactValue, onFormSubmit }) {
  if (defaultContactValue == null)
    defaultContactValue = {
      firstName: "",
      lastName: "",
      phoneNumber: "",
    };

  const [formData, updateFormData] = useState(defaultContactValue);
  const [isFormValid, setIsFormValid] = useState(false);
  const [isPhoneNumberValid, setIsPhoneNumberValid] = useState(false);
  const [hasPhoneNumberInput, setHasPhoneNumberInput] = useState(false);

  const checkIfPhoneNumberIsValid = (phoneNumber) => {
    return PHONE_NUMBER_REGEX.test(phoneNumber);
  };

  const checkIfPhoneNumberHasInput = (phoneNumber) => {
    return phoneNumber !== "";
  };

  const onFormChange = (event) => {
    var formDataWithUpdate = {};
    Object.assign(formDataWithUpdate, formData);
    formDataWithUpdate[event.target.id] = event.target.value.trim();
    updateFormData(formDataWithUpdate);
  };

  useEffect(() => {
    const phoneNumberValid = checkIfPhoneNumberIsValid(formData.phoneNumber);
    setHasPhoneNumberInput(checkIfPhoneNumberHasInput(formData.phoneNumber));
    setIsPhoneNumberValid(phoneNumberValid);
    setIsFormValid(
      phoneNumberValid && formData.firstName !== "" && formData.lastName !== ""
    );
  }, [formData]);

  return (
    <form>
      <TextField
        required
        id="firstName"
        variant="outlined"
        label="First Name"
        onChange={onFormChange}
        defaultValue={defaultContactValue.firstName}
      />
      <TextField
        required
        id="lastName"
        variant="outlined"
        label="Last Name"
        onChange={onFormChange}
        defaultValue={defaultContactValue.lastName}
      />
      <TextField
        required
        id="phoneNumber"
        variant="outlined"
        label="Phone Number"
        error={hasPhoneNumberInput && !isPhoneNumberValid}
        onChange={onFormChange}
        defaultValue={defaultContactValue.phoneNumber}
      />
      <Button
        variant="contained"
        color="primary"
        type="submit"
        disabled={!isFormValid}
        onClick={() => onFormSubmit(formData)}
      >
        Submit
      </Button>
    </form>
  );
}
