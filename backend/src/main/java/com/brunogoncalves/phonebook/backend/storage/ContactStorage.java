package com.brunogoncalves.phonebook.backend.storage;

import com.brunogoncalves.phonebook.backend.domain.Contact;
import com.brunogoncalves.phonebook.backend.domain.ContactData;
import com.brunogoncalves.phonebook.backend.storage.exception.*;

import java.util.List;

public interface ContactStorage {

    Contact create(final ContactData contactDATA) throws ContactStorageException, CouldNotInsertContactException;

    Contact get(int contactId) throws ContactStorageException, CouldNotGetContactException, CouldNotGetContactException;

    List<Contact> searchByToken(final String token) throws ContactStorageException;

    void update(final ContactData contactData, final int contactId) throws ContactStorageException, CouldNotUpdateContactException;

    void delete(final int id) throws ContactStorageException, CouldNotDeleteContactException;

}
