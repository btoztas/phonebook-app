package com.brunogoncalves.phonebook.domain;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ContactValidationTest {

    private Validator validator;

    @Before
    public void initValidator() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
    }

    @Test
    public void testContactShouldNotHaveABlankFirstName() {
        final Contact contact = new Contact("", "lastName", "+39 02 123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveANullFirstName() {
        final Contact contact = new Contact(null, "lastName", "+39 02 123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveABlankLastName() {
        final Contact contact = new Contact("firstName", "", "+39 02 123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveANullLastName() {
        final Contact contact = new Contact("firstName", null, "+39 02 123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveABlankPhoneNumber() {
        final Contact contact = new Contact("firstName", "lastName", "");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveANullPhoneNumber() {
        final Contact contact = new Contact("firstName", "lastName", null);
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveAPhoneNumberThatDoesNotStartWithAPlus() {
        final Contact contact = new Contact("firstName", "lastName", "39 02 123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveAPhoneNumberThatHasLetters() {
        final Contact contact = new Contact("firstName", "lastName", "+3l9 02 123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveAPhoneNumberThatDoesNotHaveSpacesBetweenNumberGroups() {
        final Contact contact = new Contact("firstName", "lastName", "+3902123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveAPhoneNumberThatHasMoreThanSixDigitsInLastNumberGroup() {
        final Contact contact = new Contact("firstName", "lastName", "+39 02 123456322");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveAPhoneNumberThatDoesNotHaveThreeNumberGroups() {
        final Contact contact = new Contact("firstName", "lastName", "+39 123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldBeValidForCorrectExample() {
        final Contact contact = new Contact("firstName", "lastName", "+39 02 123456");
        assertTrue(isValid(contact));
    }

    private boolean isValid(final Contact contact) {
        Set<ConstraintViolation<Contact>> violations = this.validator.validate(contact);
        return violations.isEmpty();
    }

}
