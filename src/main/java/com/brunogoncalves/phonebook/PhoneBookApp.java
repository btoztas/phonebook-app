package com.brunogoncalves.phonebook;

import com.brunogoncalves.phonebook.persistence.ContactPersistence;
import com.brunogoncalves.phonebook.persistence.ContactPersistenceException;
import com.brunogoncalves.phonebook.persistence.mysql.ContactPersistenceMysql;
import com.brunogoncalves.phonebook.resource.ContactResource;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

//TODO: Get configuration properties from a config file
//TODO: Somehow this ApplicationPath is not working
@ApplicationPath("/api")
public class PhoneBookApp extends ResourceConfig {

    public PhoneBookApp() throws ContactPersistenceException {

        //TODO: This does not seem very correct
        final ContactPersistenceMysql contactPersistence = new ContactPersistenceMysql("phonebook-user", "phonebook-password", "jdbc:mysql://mysql/phonebook");
        register(ContactResource.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(contactPersistence).to(ContactPersistence.class);
            }
        });
    }
}