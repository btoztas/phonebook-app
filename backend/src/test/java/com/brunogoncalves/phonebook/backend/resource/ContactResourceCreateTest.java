package com.brunogoncalves.phonebook.backend.resource;

import com.brunogoncalves.phonebook.backend.domain.Contact;
import com.brunogoncalves.phonebook.backend.domain.ContactData;
import com.brunogoncalves.phonebook.backend.storage.exception.ContactStorageException;
import com.brunogoncalves.phonebook.backend.storage.exception.CouldNotGetContactException;
import com.brunogoncalves.phonebook.backend.storage.exception.CouldNotInsertContactException;
import mockit.Expectations;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static com.brunogoncalves.phonebook.backend.resource.ContactResourceTestUtil.*;
import static org.junit.Assert.assertEquals;

public class ContactResourceCreateTest extends AbstractContactResourceTest {

    @Test
    public void testGetRequestReturnsExpectedContact() throws ContactStorageException, CouldNotInsertContactException {
        final ContactData expectedContactData = new ContactData("first", "last", "+213 123 123456");
        final String contactDataToCreate = newContactDataJson("first", "last", "+213 123 123456");
        final Contact createdContact = new Contact(1, "first", "last", "+213 123 123456");
        final String expectedResult = newContactJson(1, "first", "last", "+213 123 123456");

        new Expectations() {{
            contactStorage.create(withEqual(expectedContactData));
            result = createdContact;
        }};

        final Response response = target(URL_TARGET).request().post(Entity.json(contactDataToCreate));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.readEntity(String.class));
    }

    @Test
    public void testCreateContactWithInvalidContact() {
        final String jsonBody = newContactDataJson("Rick", "Morty", "+39 123456");

        final Response response = target(URL_TARGET).request().post(Entity.json(jsonBody));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCreateContactWithMissingFirstName() {
        final String jsonBody = newContactDataJson("", "Morty", "+39 23 123456");

        final Response response = target(URL_TARGET).request().post(Entity.json(jsonBody));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCreateContactWithMissingLastName() {
        final String jsonBody = newContactDataJson("Rick", "", "+39 23 123456");

        final Response response = target(URL_TARGET).request().post(Entity.json(jsonBody));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

}
