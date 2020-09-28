package com.brunogoncalves.phonebook.backend.storage;

import com.brunogoncalves.phonebook.backend.domain.Contact;

import java.util.List;

public interface ContactStorage {

    void create(final Contact contact) throws ContactStorageException;

    List<Contact> searchByToken(final String name) throws ContactStorageException;

    void update(final Contact contact) throws ContactStorageException;

    void delete(final String number) throws ContactStorageException;

}
