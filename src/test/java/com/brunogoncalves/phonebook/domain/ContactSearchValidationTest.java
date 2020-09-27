package com.brunogoncalves.phonebook.domain;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class ContactSearchValidationTest {

    private Validator validator;

    @Before
    public void initValidator() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
    }

    @Test
    public void testContactShouldNotHaveABlankToken() {
        final ContactSearchRequest contactSearchRequest = new ContactSearchRequest("");
        assertFalse(isValid(contactSearchRequest));
    }

    @Test
    public void testContactShouldNotHaveANullToken() {
        final ContactSearchRequest contactSearchRequest = new ContactSearchRequest(null);
        assertFalse(isValid(contactSearchRequest));
    }

    private boolean isValid(final ContactSearchRequest contactSearchRequest) {
        Set<ConstraintViolation<ContactSearchRequest>> violations = this.validator.validate(contactSearchRequest);
        return violations.isEmpty();
    }
}
