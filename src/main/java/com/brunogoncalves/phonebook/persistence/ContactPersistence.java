package com.brunogoncalves.phonebook.persistence;

import com.brunogoncalves.phonebook.domain.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactPersistence {

    //TODO: Possibly return something?
    Contact create(final Contact contact) throws ContactPersistenceException;

    Optional<Contact> getByNumber(final String number) throws ContactPersistenceException;

    List<Contact> getByName(final String name) throws ContactPersistenceException;

    Contact update(final Contact contact) throws ContactPersistenceException;

    void delete(final String number) throws ContactPersistenceException;

}
