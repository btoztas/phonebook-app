package com.brunogoncalves.phonebook.resource;

import com.brunogoncalves.phonebook.domain.Contact;
import com.brunogoncalves.phonebook.storage.ContactStorage;
import com.brunogoncalves.phonebook.storage.ContactStorageException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

//TODO: Search Request Test missing
public class ContactResourceTest extends JerseyTest {

    private static final String URL_TARGET = "/contact";

    private static final String URL_TARGET_SEARCH = URL_TARGET + "/search";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mocked
    private ContactStorage contactStorage;

    @Test
    public void testCreateContactWithValidData() throws ContactStorageException {
        final Entity<String> jsonBody = newContactJson("Rick", "Morty", "+39 02 123456");
        final Contact expectedContact = new Contact("Rick", "Morty", "+39 02 123456");

        final Response response = target(URL_TARGET).request().post(jsonBody);

        new Verifications() {{
            contactStorage.create(withEqual(expectedContact));
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
    public void testUpdateContactWithValidContact() throws ContactStorageException {
        final Entity<String> jsonBody = newContactJson("Rick", "Morty", "+39 02 123456");
        final Contact expectedContact = new Contact("Rick", "Morty", "+39 02 123456");

        final Response response = target(URL_TARGET).request().put(jsonBody);

        new Verifications() {{
            contactStorage.update(withEqual(expectedContact));
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

    @Test
    public void testSearchRequestWithMultipleResults() throws ContactStorageException, JsonProcessingException {
        final String token = "token";
        final Entity<String> jsonBody = newSearchRequestJson(token);
        final List<Map<String, String>> queryResults = ImmutableList.of(
                ImmutableMap.of(
                        "first_name", "first1",
                        "last_name", "last1",
                        "phone_number", "number1"
                ),
                ImmutableMap.of(
                        "first_name", "first2",
                        "last_name", "last2",
                        "phone_number", "number2"
                )
        );
        final String expectedResults = jsonFromQueryResults(queryResults);

        new Expectations() {{
            contactStorage.searchByToken(withEqual(token));
            result = queryResults;
        }};

        final Response response = target(URL_TARGET_SEARCH).request().post(jsonBody);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResults, response.readEntity(String.class));
    }

    @Test
    public void testSearchRequestWithZeroResults() throws ContactStorageException, JsonProcessingException {
        final String token = "token";
        final Entity<String> jsonBody = newSearchRequestJson(token);
        final List<Map<String, String>> queryResults = ImmutableList.of();
        final String expectedResults = jsonFromQueryResults(queryResults);

        new Expectations() {{
            contactStorage.searchByToken(withEqual(token));
            result = queryResults;
        }};

        final Response response = target(URL_TARGET_SEARCH).request().post(jsonBody);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResults, response.readEntity(String.class));
    }

    @Test
    public void testSearchRequestWithInvalidRequest() throws ContactStorageException, JsonProcessingException {
        final String token = "";
        final Entity<String> jsonBody = newSearchRequestJson(token);

        final Response response = target(URL_TARGET_SEARCH).request().post(jsonBody);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    /**
     * Configures the test to inject {@link #contactStorage} mock into the {@link ContactResource} under test.
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
                        bind(contactStorage).to(ContactStorage.class);
                    }
                });
    }

    private String jsonFromQueryResults(final List<Map<String, String>> queryResults) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(queryResults);
    }
    
    private Entity<String> newContactJson(final String firstName, final String lastName, final String phoneNumber) {
        return Entity.json(String.format("{\"firstName\":\"%s\",\"lastName\":\"%s\",\"phoneNumber\":\"%s\"}",
                                         firstName, lastName, phoneNumber));
    }

    private Entity<String> newDeleteRequestJson(final String phoneNumber) {
        return Entity.json(String.format("{\"phoneNumber\":\"%s\"}", phoneNumber));
    }

    private Entity<String> newSearchRequestJson(final String token) {
        return Entity.json(String.format("{\"token\":\"%s\"}", token));
    }
}
