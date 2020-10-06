package com.brunogoncalves.phonebook.backend.storage;

import com.brunogoncalves.phonebook.backend.domain.Contact;
import com.brunogoncalves.phonebook.backend.domain.ContactData;
import com.brunogoncalves.phonebook.backend.storage.exception.*;

import java.util.List;

/**
 * The interface that will be used by the resources to interact with the Contact Persistence Layer.
 */
public interface ContactStorage {

    Contact create(final ContactData contactDATA) throws ContactStorageException, CouldNotInsertContactException;

    Contact get(int contactId) throws ContactStorageException, CouldNotGetContactException;

    List<Contact> searchByToken(final String token) throws ContactStorageException;

    void update(final ContactData contactData, final int contactId) throws ContactStorageException, CouldNotUpdateContactException;

}
