package com.brunogoncalves.phonebook.backend.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Entity;
import java.util.List;
import java.util.Map;

public class ContactResourceTestUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static final String URL_TARGET = "/contact";

    static String newContactDataJson(final String firstName, final String lastName, final String phoneNumber) {
        return String.format("{\"firstName\":\"%s\",\"lastName\":\"%s\",\"phoneNumber\":\"%s\"}",
                firstName, lastName, phoneNumber);
    }

    static String newContactJson(final int id, final String firstName, final String lastName, final String phoneNumber) {
        return String.format("{\"id\":%d,\"firstName\":\"%s\",\"lastName\":\"%s\",\"phoneNumber\":\"%s\"}",
                id, firstName, lastName, phoneNumber);
    }

    static Entity<String> newSearchRequestJson(final String token) {
        return Entity.json(String.format("{\"token\":\"%s\"}", token));
    }

    static String listOfContactsJson(final List<Map<String, Object>> queryResults) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(queryResults);
    }

}
