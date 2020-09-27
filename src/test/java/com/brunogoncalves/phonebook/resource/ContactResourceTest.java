package com.brunogoncalves.phonebook.resource;

import com.brunogoncalves.phonebook.domain.Contact;
import com.brunogoncalves.phonebook.persistence.ContactPersistence;
import com.brunogoncalves.phonebook.persistence.ContactPersistenceException;
import mockit.Mocked;
import mockit.Verifications;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

//TODO: Search Request Test missing
public class ContactResourceTest extends JerseyTest {

    private static final String URL_TARGET = "/contact";
    
    @Mocked
    private ContactPersistence contactPersistence;

    /**
     * Configures the test to inject {@link #contactPersistence} mock into the {@link ContactResource} under test.
     *
     * @return the instance of {@link Application} containing the resource {@link ContactResource} under test.
     */
    @Override
    protected Application configure() {
        return new ResourceConfig()
                .register(ContactResource.class)
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(contactPersistence).to(ContactPersistence.class);
                    }
                });
    }

    @Test
    public void testCreateContactWithValidData() throws ContactPersistenceException {
        final Entity<String> jsonBody = newContactJson("Rick", "Morty", "+39 02 123456");
        final Contact expectedContact = new Contact("Rick", "Morty", "+39 02 123456");

        final Response response = target(URL_TARGET).request().post(jsonBody);

        new Verifications() {{
            contactPersistence.create(withEqual(expectedContact));
            times = 1;
        }};
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedContact, response.readEntity(Contact.class));
    }

    @Test
    public void testCreateContactWithInvalidContact() {
        final Entity<String> jsonBody = newContactJson("Rick", "Morty", "+39 123456");

        final Response response = target(URL_TARGET).request().post(jsonBody);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCreateContactWithMissingFirstName() {
        final Entity<String> jsonBody = newContactJson("", "Morty", "+39 23 123456");

        final Response response = target(URL_TARGET).request().post(jsonBody);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCreateContactWithMissingLastName() {
        final Entity<String> jsonBody = newContactJson("Rick", "", "+39 23 123456");

        final Response response = target(URL_TARGET).request().post(jsonBody);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testUpdateContactWithValidContact() throws ContactPersistenceException {
        final Entity<String> jsonBody = newContactJson("Rick", "Morty", "+39 02 123456");
        final Contact expectedContact = new Contact("Rick", "Morty", "+39 02 123456");

        final Response response = target(URL_TARGET).request().put(jsonBody);

        new Verifications() {{
            contactPersistence.update(withEqual(expectedContact));
            times = 1;
        }};
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedContact, response.readEntity(Contact.class));
    }

    @Test
    public void testUpdateContactWithInvalidContact() {
        final Entity<String> jsonBody = newContactJson("Rick", "Morty", "+39 123456");

        final Response response = target(URL_TARGET).request().put(jsonBody);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testUpdateContactWithMissingFirstName() {
        final Entity<String> jsonBody = newContactJson("", "Morty", "+39 23 123456");

        final Response response = target(URL_TARGET).request().put(jsonBody);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testUpdateContactWithMissingLastName() {
        final Entity<String> jsonBody = newContactJson("Rick", "", "+39 23 123456");

        final Response response = target(URL_TARGET).request().put(jsonBody);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
    
    /**
    @Test
    public void testDeleteContactWithInvalidPhoneNumber() throws ContactPersistenceException {
        final String phoneNumber = "39 02 123456";
        final Entity<String> jsonBody = newDeleteRequestJson(phoneNumber);

        final Response response = target(URL_TARGET).request().put(jsonBody);

        new Verifications() {{
            contactPersistence.delete(withEqual(phoneNumber));
            times = 1;
        }};
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
    **/
    private Entity<String> newContactJson(final String firstName, final String lastName, final String phoneNumber) {
        return Entity.json(String.format("{\"firstName\":\"%s\",\"lastName\":\"%s\",\"phoneNumber\":\"%s\"}",
                firstName, lastName, phoneNumber));
    }

    private Entity<String> newDeleteRequestJson(final String phoneNumber) {
        return Entity.json(String.format("{\"phoneNumber\":\"%s\"}", phoneNumber));
    }
}
