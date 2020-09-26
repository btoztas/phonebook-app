package com.brunogoncalves.phonebook;

import com.brunogoncalves.phonebook.persistence.PhoneNumberPersistence;
import com.brunogoncalves.phonebook.persistence.mysql.PhoneNumberPersistenceMysql;
import com.brunogoncalves.phonebook.resource.PhoneBookResource;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

//TODO: Somehow this is not working
@ApplicationPath("/api")
public class PhoneBookApp extends ResourceConfig {

    public PhoneBookApp() {
        register(PhoneBookResource.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(PhoneNumberPersistenceMysql.class)
                        .to(PhoneNumberPersistence.class)
                        .in(Singleton.class);
            }
        });
    }
}