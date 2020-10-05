package com.brunogoncalves.phonebook.backend.resource;

import com.brunogoncalves.phonebook.backend.storage.ContactStorage;
import mockit.Mocked;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import javax.ws.rs.core.Application;

public class AbstractContactResourceTest extends JerseyTest {

    @Mocked
    protected ContactStorage contactStorage;

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
}
