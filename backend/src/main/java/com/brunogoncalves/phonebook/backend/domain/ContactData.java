package com.brunogoncalves.phonebook.backend.domain;

import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * The model of a contact in the Phone Book.
 */
public class ContactData {

    @NotNull(message = "The contact must have a first name")
    @NotBlank(message = "The contact must have a first name")
    private String firstName;

    @NotNull(message = "The contact must have a last name")
    @NotBlank(message = "The contact must have a last name")
    private String lastName;

    @NotNull(message = "The contact must have a phone number")
    @NotBlank(message = "The contact must have a phone number")
    @Pattern(regexp = "^[+][0-9]+[ ][0-9]+[ ][0-9]{6}$", message = "Phone Number does not obey the formatting rules")
    private String phoneNumber;

    public ContactData() {
    }

    public ContactData(final String firstName, final String lastName, final String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phoneNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactData that = (ContactData) o;
        return firstName.equals(that.firstName) &&
                lastName.equals(that.lastName) &&
                phoneNumber.equals(that.phoneNumber);
    }
}
