package com.brunogoncalves.phonebook.backend.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Objects;

/**
 * The domain model for a Contact: it contains an id and {@link ContactData}. The contact data is unwrapped for
 * the purpose of Serialization and Deserialization.
 */
public class Contact {

    private int id;

    @JsonUnwrapped
    private ContactData contactData;

    public Contact() {
    }

    public Contact(final int id, final String firstName, final String lastName, final String phoneNumber) {
        this.id = id;
        this.contactData = new ContactData(firstName, lastName, phoneNumber);
    }

    public Contact(final int id, final ContactData contactData) {
        this.id = id;
        this.contactData = contactData;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public ContactData getContactData() {
        return contactData;
    }

    public void setContactData(ContactData contactData) {
        this.contactData = contactData;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", contactData=" + contactData +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return id == contact.id &&
                contactData.equals(contact.contactData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contactData);
    }
}
