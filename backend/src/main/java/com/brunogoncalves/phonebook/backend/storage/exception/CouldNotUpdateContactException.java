package com.brunogoncalves.phonebook.backend.storage.exception;

import com.brunogoncalves.phonebook.backend.domain.ContactData;

/**
 * To be thrown whenever there is an issue Updating a Contact
 * through {@link com.brunogoncalves.phonebook.backend.storage.ContactStorage#update(ContactData, int)}
 */
public class CouldNotUpdateContactException extends Throwable {
}
