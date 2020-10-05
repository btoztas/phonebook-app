import axios from "axios";
import { properties } from "../../../properties.js";

const CONTACT_ENDPOINT = "/contact";
const API_URL = properties.backendUrl + CONTACT_ENDPOINT;

export const editContactRequest = (newContactData) => {
  if (newContactData != null) {
    const contactData = {
      firstName: newContactData.firstName,
      lastName: newContactData.lastName,
      phoneNumber: newContactData.phoneNumber,
    };
    return axios.put(API_URL + "/" + newContactData.id, contactData);
  }
  throw new Error("The Contact Data must be provided");
};
