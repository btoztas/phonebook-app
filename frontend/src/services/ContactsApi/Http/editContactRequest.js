import axios from "axios";
import { properties } from "../../../properties.js";

const CONTACT_ENDPOINT = "/contact";
const API_URL = properties.backendUrl + CONTACT_ENDPOINT;

export const editContactRequest = (contactData) => {
  if (contactData) {
    return axios.put(API_URL, contactData);
  }
  throw new Error("The Contact Data must be provided");
};
