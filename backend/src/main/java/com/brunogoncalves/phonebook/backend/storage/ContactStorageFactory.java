package com.brunogoncalves.phonebook.backend.storage;

import com.brunogoncalves.phonebook.backend.storage.mysql.ContactStorageMysql;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Factory that returns a {@link ContactStorageFactory} accordingly to the database properties defined.
 */
public class ContactStorageFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactStorageFactory.class);

    public ContactStorage getContactStorage(final Properties properties) throws ContactStorageException {
        final String dbType = properties.getProperty("db.type");

        switch (dbType) {
            case "mysql":
                return new ContactStorageMysql(properties.getProperty("db.user"),
                                               properties.getProperty("db.password"),
                                               properties.getProperty("db.jdbc"));
            default:
                LOGGER.error("DB Type {} is not implemented", dbType);
                throw new IllegalStateException();
        }
    }
}
