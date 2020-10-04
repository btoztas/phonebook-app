import axios from "axios";
import { properties } from "../../../properties.js";

const CONTACT_ENDPOINT = "/contact/search";
const API_URL = properties.backendUrl + CONTACT_ENDPOINT;

export const searchContactRequest = (searchToken) => {
  if (searchToken) {
    const requestBody = { token: searchToken };
    return axios.post(API_URL, requestBody);
  }
  throw new Error("A Search Token must be provided");
};
