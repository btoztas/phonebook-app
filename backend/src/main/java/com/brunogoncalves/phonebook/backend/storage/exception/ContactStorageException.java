package com.brunogoncalves.phonebook.backend.storage.exception;

/**
 * To be thrown as a wrapper to Exceptions thrown by the Storage Layer.
 */
public class ContactStorageException extends Exception{
    public ContactStorageException(Exception e) {
        super(e);
    }
}
