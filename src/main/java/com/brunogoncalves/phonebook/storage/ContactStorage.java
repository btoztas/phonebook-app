package com.brunogoncalves.phonebook.storage;

import com.brunogoncalves.phonebook.domain.Contact;

import java.util.List;

public interface ContactStorage {

    void create(final Contact contact) throws ContactStorageException;

    List<Contact> searchByToken(final String name) throws ContactStorageException;

    void update(final Contact contact) throws ContactStorageException;

    void delete(final String number) throws ContactStorageException;

}
