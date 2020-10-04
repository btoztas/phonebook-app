import axios from "axios";
import { properties } from "../../../properties.js";

const CONTACT_ENDPOINT = "/contact";
const API_URL = properties.backendUrl + CONTACT_ENDPOINT;

export const newContactRequest = (contactData) => {
  if (contactData) {
    return axios.post(API_URL, contactData);
  }
  throw new Error("The Contact Data must be provided");
};
