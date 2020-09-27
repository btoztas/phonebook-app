package com.brunogoncalves.phonebook.persistence.mysql;

import com.brunogoncalves.phonebook.domain.Contact;
import com.brunogoncalves.phonebook.persistence.ContactPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class ContactPersistenceMysql implements ContactPersistence {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactPersistenceMysql.class);
    //TODO: Somewhere here we will need a DB Conn Pool

    @Override
    public Contact create(final Contact contact) {

        LOGGER.info("Creating {}", contact);
        return contact;
    }

    @Override
    public Optional<Contact> getByNumber(final String number) {
        LOGGER.info("Getting {}", number);
        return Optional.empty();
    }

    @Override
    public List<Contact> getByName(final String name) {
        return null;
    }

    @Override
    public Contact update(final Contact contact) {
        return contact;
    }

    @Override
    public void delete(final String number) {

    }
}
