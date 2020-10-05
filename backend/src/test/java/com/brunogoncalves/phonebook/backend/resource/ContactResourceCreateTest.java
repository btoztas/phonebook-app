package com.brunogoncalves.phonebook.backend.resource;

import com.brunogoncalves.phonebook.backend.domain.Contact;
import com.brunogoncalves.phonebook.backend.domain.ContactData;
import com.brunogoncalves.phonebook.backend.storage.exception.ContactStorageException;
import com.brunogoncalves.phonebook.backend.storage.exception.CouldNotInsertContactException;
import mockit.Expectations;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static com.brunogoncalves.phonebook.backend.resource.ContactResourceTestUtil.*;
import static org.junit.Assert.assertEquals;

public class ContactResourceCreateTest extends AbstractContactResourceTest {

    @Test
    public void testCreateContactWithValidData() throws ContactStorageException, CouldNotInsertContactException {
        final String jsonBody = newContactDataJson("Rick", "Morty", "+39 02 123456");
        final ContactData expectedContactData = new ContactData("Rick", "Morty", "+39 02 123456");
        final String expectedContactJsonResponse = newContactJson(1, "Rick", "Morty", "+39 02 123456");
        final Contact expectedContact = new Contact(1, expectedContactData);

        final Response response = target(URL_TARGET).request().post(Entity.json(jsonBody));

        new Expectations() {{
            contactStorage.create(withEqual(expectedContactData));
            result = expectedContact;
            times = 1;
        }};

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedContactJsonResponse, response.readEntity(String.class));
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
