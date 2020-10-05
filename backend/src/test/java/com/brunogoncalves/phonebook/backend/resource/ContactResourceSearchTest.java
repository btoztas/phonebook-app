package com.brunogoncalves.phonebook.backend.resource;

import com.brunogoncalves.phonebook.backend.storage.ContactStorage;
import com.brunogoncalves.phonebook.backend.storage.exception.ContactStorageException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static com.brunogoncalves.phonebook.backend.resource.ContactResourceTestUtil.*;
import static org.junit.Assert.assertEquals;

public class ContactResourceSearchTest extends AbstractContactResourceTest {

    static final String URL_TARGET_SEARCH = URL_TARGET + "/search";

    @Test
    public void testSearchRequestWithMultipleResults() throws ContactStorageException, JsonProcessingException {
        final String token = "token";
        final Entity<String> jsonBody = newSearchRequestJson(token);
        final List<Map<String, Object>> queryResults = ImmutableList.of(
                ImmutableMap.of(
                        "id", 1,
                        "first_name", "first1",
                        "last_name", "last1",
                        "phone_number", "number1"
                ),
                ImmutableMap.of(
                        "id", 2,
                        "first_name", "first2",
                        "last_name", "last2",
                        "phone_number", "number2"
                )
        );
        final String expectedResults = listOfContactsJson(queryResults);

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
        final List<Map<String, Object>> queryResults = ImmutableList.of();
        final String expectedResults = listOfContactsJson(queryResults);

        new Expectations() {{
            contactStorage.searchByToken(withEqual(token));
            result = queryResults;
        }};

        final Response response = target(URL_TARGET_SEARCH).request().post(jsonBody);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResults, response.readEntity(String.class));
    }

    @Test
    public void testSearchRequestWithInvalidRequest() {
        final String token = "";
        final Entity<String> jsonBody = newSearchRequestJson(token);

        final Response response = target(URL_TARGET_SEARCH).request().post(jsonBody);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

}
