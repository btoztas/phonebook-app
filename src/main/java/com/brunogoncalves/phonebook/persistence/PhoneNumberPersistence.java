package com.brunogoncalves.phonebook.persistence;

import com.brunogoncalves.phonebook.model.PhoneNumber;

import java.util.List;
import java.util.Optional;

public interface PhoneNumberPersistence {

    //TODO: Possibly return something?
    boolean create(PhoneNumber phoneNumber);

    Optional<PhoneNumber> getByNumber(String number);

    List<PhoneNumber> getByName(String name);

    void update(PhoneNumber phoneNumber);

    void delete(String number);

}
