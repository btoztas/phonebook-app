import axios from "axios";
import { properties } from "../../../properties.js";

const CONTACT_ENDPOINT = "/contact";
const API_URL = properties.backendUrl + CONTACT_ENDPOINT;

export const getContactRequest = (contactId) => {
  if (contactId) {
    return axios.get(API_URL + "/" + contactId);
  }
  throw new Error("The Contact Id must be provided");
};
