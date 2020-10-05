package com.brunogoncalves.phonebook.backend.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class ContactSerializationTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void testContactIsCorrectlySerialized() throws JsonProcessingException {
        final Contact contact = new Contact(1, "Harry", "Granger", "+232 213 212345");
        final String expectedContactSerialized =
                String.format("{\"id\":%d,\"firstName\":\"%s\",\"lastName\":\"%s\",\"phoneNumber\":\"%s\"}",
                        1, "Harry", "Granger", "+232 213 212345");

        final String contactSerialized = OBJECT_MAPPER.writeValueAsString(contact);

        Assert.assertEquals(expectedContactSerialized, contactSerialized);
    }

    @Test
    public void testContactIsCorrectlyDeserialized() throws JsonProcessingException {
        final String contactJson =
                String.format("{\"id\":%d,\"firstName\":\"%s\",\"lastName\":\"%s\",\"phoneNumber\":\"%s\"}",
                        1, "Harry", "Granger", "+232 213 212345");
        final Contact expectedContact = new Contact(1, "Harry", "Granger", "+232 213 212345");

        final Contact contactDeserialized = OBJECT_MAPPER.readValue(contactJson, Contact.class);

        Assert.assertEquals(expectedContact, contactDeserialized);
    }
}
