package com.brunogoncalves.phonebook.backend.storage.exception;

import com.brunogoncalves.phonebook.backend.domain.ContactData;

/**
 * To be thrown whenever there is an issue Creating a Contact
 * through {@link com.brunogoncalves.phonebook.backend.storage.ContactStorage#create(ContactData)}}
 */
public class CouldNotInsertContactException extends Throwable {
}
