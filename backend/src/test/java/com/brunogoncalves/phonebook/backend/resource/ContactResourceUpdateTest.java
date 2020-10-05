package com.brunogoncalves.phonebook.backend.resource;

import com.brunogoncalves.phonebook.backend.domain.ContactData;
import com.brunogoncalves.phonebook.backend.storage.exception.ContactStorageException;
import com.brunogoncalves.phonebook.backend.storage.exception.CouldNotUpdateContactException;
import mockit.Verifications;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static com.brunogoncalves.phonebook.backend.resource.ContactResourceTestUtil.URL_TARGET;
import static com.brunogoncalves.phonebook.backend.resource.ContactResourceTestUtil.newContactDataJson;
import static org.junit.Assert.assertEquals;

public class ContactResourceUpdateTest extends AbstractContactResourceTest {

    final static int CONTACT_ID = 1;
    final static String UPDATE_URL_TARGET = URL_TARGET + "/" + CONTACT_ID;

    @Test
    public void testUpdateContactWithValidContact() throws ContactStorageException, CouldNotUpdateContactException {
        final String jsonBody = newContactDataJson("Rick", "Morty", "+39 02 123456");
        final ContactData expectedContact = new ContactData("Rick", "Morty", "+39 02 123456");

        final Response response = target().request(UPDATE_URL_TARGET).put(Entity.json(jsonBody));

        new Verifications() {{
            contactStorage.update(withEqual(expectedContact), withEqual(CONTACT_ID));
            times = 1;
        }};
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        assertEquals(expectedContact, response.readEntity(ContactData.class));
    }

    @Test
    public void testUpdateContactWithInvalidContact() {
        final String jsonBody = newContactDataJson("Rick", "Morty", "+39 123456");

        final Response response = target(UPDATE_URL_TARGET).request().put(Entity.json(jsonBody));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testUpdateContactWithMissingFirstName() {
        final String jsonBody = newContactDataJson("", "Morty", "+39 23 123456");

        final Response response = target(UPDATE_URL_TARGET).request().put(Entity.json(jsonBody));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testUpdateContactWithMissingLastName() {
        final String jsonBody = newContactDataJson("Rick", "", "+39 23 123456");

        final Response response = target(UPDATE_URL_TARGET).request().put(Entity.json(jsonBody));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

}
