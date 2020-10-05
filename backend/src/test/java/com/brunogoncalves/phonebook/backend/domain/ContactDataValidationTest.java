package com.brunogoncalves.phonebook.backend.domain;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ContactDataValidationTest {

    private Validator validator;

    @Before
    public void initValidator() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
    }

    @Test
    public void testContactShouldNotHaveABlankFirstName() {
        final ContactData contact = new ContactData("", "lastName", "+39 02 123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveANullFirstName() {
        final ContactData contact = new ContactData(null, "lastName", "+39 02 123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveABlankLastName() {
        final ContactData contact = new ContactData("firstName", "", "+39 02 123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveANullLastName() {
        final ContactData contact = new ContactData("firstName", null, "+39 02 123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveABlankPhoneNumber() {
        final ContactData contact = new ContactData("firstName", "lastName", "");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveANullPhoneNumber() {
        final ContactData contact = new ContactData("firstName", "lastName", null);
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveAPhoneNumberThatDoesNotStartWithAPlus() {
        final ContactData contact = new ContactData("firstName", "lastName", "39 02 123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveAPhoneNumberThatHasLetters() {
        final ContactData contact = new ContactData("firstName", "lastName", "+3l9 02 123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveAPhoneNumberThatDoesNotHaveSpacesBetweenNumberGroups() {
        final ContactData contact = new ContactData("firstName", "lastName", "+3902123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveAPhoneNumberThatHasMoreThanSixDigitsInLastNumberGroup() {
        final ContactData contact = new ContactData("firstName", "lastName", "+39 02 123456322");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldNotHaveAPhoneNumberThatDoesNotHaveThreeNumberGroups() {
        final ContactData contact = new ContactData("firstName", "lastName", "+39 123456");
        assertFalse(isValid(contact));
    }

    @Test
    public void testContactShouldBeValidForCorrectExample() {
        final ContactData contact = new ContactData("firstName", "lastName", "+39 02 123456");
        assertTrue(isValid(contact));
    }

    private boolean isValid(final ContactData contact) {
        Set<ConstraintViolation<ContactData>> violations = this.validator.validate(contact);
        return violations.isEmpty();
    }

}
