package com.brunogoncalves.phonebook.model;

import java.util.Objects;

public class PhoneNumber {

    private final String firstName;

    private final String lastName;

    private final String number;

    public PhoneNumber(String firstName, String lastName, String number) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return firstName.equals(that.firstName) &&
                lastName.equals(that.lastName) &&
                number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, number);
    }
}
