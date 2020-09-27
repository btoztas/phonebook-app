package com.brunogoncalves.phonebook.persistence;

import java.sql.SQLException;

public class ContactPersistenceException extends Exception{
    public ContactPersistenceException(Exception e) {
        super(e);
    }
}
