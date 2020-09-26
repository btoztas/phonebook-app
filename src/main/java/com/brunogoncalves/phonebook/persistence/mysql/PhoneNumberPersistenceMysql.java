package com.brunogoncalves.phonebook.persistence.mysql;

import com.brunogoncalves.phonebook.model.PhoneNumber;
import com.brunogoncalves.phonebook.persistence.PhoneNumberPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class PhoneNumberPersistenceMysql implements PhoneNumberPersistence {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneNumberPersistenceMysql.class);
    //TODO: Somewhere here we will need a DB Conn Pool

    @Override
    public boolean create(PhoneNumber phoneNumber) {
        LOGGER.info("Creating {}", phoneNumber);
        return true;
    }

    @Override
    public Optional<PhoneNumber> getByNumber(String number) {
        LOGGER.info("Getting {}", number);
        return Optional.empty();
    }

    @Override
    public List<PhoneNumber> getByName(String name) {
        return null;
    }

    @Override
    public void update(PhoneNumber phoneNumber) {

    }

    @Override
    public void delete(String number) {

    }
}
