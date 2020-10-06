package com.brunogoncalves.phonebook.backend;

import com.brunogoncalves.phonebook.backend.filter.CORSFilter;
import com.brunogoncalves.phonebook.backend.resource.ContactResource;
import com.brunogoncalves.phonebook.backend.storage.ContactStorage;
import com.brunogoncalves.phonebook.backend.storage.exception.ContactStorageException;
import com.brunogoncalves.phonebook.backend.storage.ContactStorageFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

public class PhoneBookApp extends ResourceConfig {

    private static final String PROPERTIES_FILE = "config.properties";

    private static final Properties properties = new Properties();

    private static final List<String> MANDATORY_PROPERTIES = new LinkedList<String>(){{
        add("db.user");
        add("db.password");
        add("db.jdbc");
        add("db.type");
    }};

    public PhoneBookApp() throws ContactStorageException {

        readProperties();
        validateProperties();

        final ContactStorage contactStorage = (new ContactStorageFactory()).getContactStorage(properties);

        register(new CORSFilter());
        register(ContactResource.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(contactStorage).to(ContactStorage.class);
            }
        });
    }

    private void validateProperties() {
        for(String prop : MANDATORY_PROPERTIES)
            if(!properties.containsKey(prop))
                throw new IllegalStateException("Property with name {} is mandatory");
    }

    private void readProperties() {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to read properties file", e);
            }
        }
    }
}