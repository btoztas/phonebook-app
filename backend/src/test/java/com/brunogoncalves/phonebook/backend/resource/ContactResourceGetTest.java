package com.brunogoncalves.phonebook.backend.resource;

import com.brunogoncalves.phonebook.backend.domain.Contact;
import com.brunogoncalves.phonebook.backend.storage.exception.ContactStorageException;
import com.brunogoncalves.phonebook.backend.storage.exception.CouldNotGetContactException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import mockit.Expectations;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static com.brunogoncalves.phonebook.backend.resource.ContactResourceTestUtil.*;
import static org.junit.Assert.assertEquals;

public class ContactResourceGetTest extends AbstractContactResourceTest {

    @Test
    public void testGetRequestReturnsExpectedContact() throws ContactStorageException, CouldNotGetContactException {
        final int contactId = 1;
        final Contact expectedContact = new Contact(1, "first", "last", "number");
        final String expectedResult = newContactJson(1, "first", "last", "number");

        new Expectations() {{
            contactStorage.get(withEqual(contactId));
            result = expectedContact;
        }};

        final Response response = target(URL_TARGET + "/" + contactId).request().get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.readEntity(String.class));
    }
}
