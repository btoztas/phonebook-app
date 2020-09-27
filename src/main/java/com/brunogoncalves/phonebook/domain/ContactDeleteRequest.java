package com.brunogoncalves.phonebook.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * A POJO representing the necessary data for a Contact Delete Request.
 */
public class ContactDeleteRequest {

    @NotNull(message = "The contact must have a phone number")
    @NotBlank(message = "The contact must have a phone number")
    @Pattern(regexp = "^[+][0-9]+[ ][0-9]+[ ][0-9]{6}$", message = "Phone Number does not obey the formatting rules")
    private String phoneNumber;

    public ContactDeleteRequest() {
    }

    public ContactDeleteRequest(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactDeleteRequest that = (ContactDeleteRequest) o;
        return Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber);
    }

    @Override
    public String toString() {
        return "PhoneNumberDeleteRequest{" +
                "phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
