import React, { useState, useEffect } from "react";

import { TextField, Button } from "@material-ui/core";

const PHONE_NUMBER_REGEX = new RegExp("^[+][0-9]+[ ][0-9]+[ ][0-9]{6}$");

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
  const [hasPhoneNumberInput, sethasPhoneNumberInput] = useState(false);

  const checkPhoneNumber = (phoneNumberInput) => {
    if (phoneNumberInput === "") sethasPhoneNumberInput(false);
    else {
      sethasPhoneNumberInput(true);
      setIsPhoneNumberValid(PHONE_NUMBER_REGEX.test(phoneNumberInput));
    }
  };

  const onFormChange = (event) => {
    var formDataToUpdate = formData;
    formDataToUpdate[event.target.id] = event.target.value.trim();
    updateFormData(formDataToUpdate);
  };

  useEffect(() => {
    checkPhoneNumber(formData.phoneNumber);
    setIsFormValid(
      isPhoneNumberValid &&
        formData.firstName !== "" &&
        formData.lastName !== ""
    );
  }, [
    formData.firstName,
    formData.lastName,
    formData.phoneNumber,
    isPhoneNumberValid,
  ]);

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
        disabled={!isFormValid}
        onClick={onFormSubmit}
      >
        Add Contact
      </Button>
    </form>
  );
}
