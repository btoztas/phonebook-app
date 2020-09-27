package com.brunogoncalves.phonebook;

import com.brunogoncalves.phonebook.persistence.ContactPersistence;
import com.brunogoncalves.phonebook.persistence.mysql.ContactPersistenceMysql;
import com.brunogoncalves.phonebook.resource.ContactResource;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

//TODO: Get configuration properties from a config file8
//TODO: Somehow this ApplicationPath is not working
@ApplicationPath("/api")
public class PhoneBookApp extends ResourceConfig {

    public PhoneBookApp() {
        register(ContactResource.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(ContactPersistenceMysql.class)
                        .to(ContactPersistence.class)
                        .in(Singleton.class);
            }
        });
    }
}