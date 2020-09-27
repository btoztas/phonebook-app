package com.brunogoncalves.phonebook.resource;

import com.brunogoncalves.phonebook.domain.Contact;
import com.brunogoncalves.phonebook.domain.ContactSearchRequest;
import com.brunogoncalves.phonebook.storage.ContactStorage;
import com.brunogoncalves.phonebook.storage.ContactStorageException;
import com.brunogoncalves.phonebook.storage.mysql.ContactStorageMysql;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/contact")
@Singleton
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContactResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactStorageMysql.class);

    private static final String CONTACT_RESOURCE_DESCRIPTION = "ContactResource";

    @Inject
    private ContactStorage contactStorage;

    @POST
    public Response create(@NotNull @Valid final Contact contact) {
        LOGGER.info("[{}] Create {}", CONTACT_RESOURCE_DESCRIPTION, contact.toString());
        try {
            contactStorage.create(contact);
        } catch (ContactStorageException e) {
            LOGGER.error("[{}] Could not create {}", CONTACT_RESOURCE_DESCRIPTION, contact.toString(), e);
            return Response.serverError().build();
        }
        return Response.ok(contact).build();
    }

    @PUT
    public Response update(@NotNull @Valid final Contact contact) {
        LOGGER.info("[{}] Update {}", CONTACT_RESOURCE_DESCRIPTION, contact.toString());
        try {
            contactStorage.update(contact);
        } catch (ContactStorageException e) {
            LOGGER.error("[{}] Could not perform update {}", CONTACT_RESOURCE_DESCRIPTION, contact.toString(), e);
            return Response.serverError().build();
        }
        return Response.ok(contact).build();
    }

    @POST
    @Path("/search")
    public Response search(@NotNull @Valid final ContactSearchRequest contactSearchRequest) {
        LOGGER.info("[{}] Search {}", CONTACT_RESOURCE_DESCRIPTION, contactSearchRequest);
        List<Contact> contacts;
        try {
            contacts = contactStorage.searchByToken(contactSearchRequest.getToken());
        } catch (ContactStorageException e) {
            LOGGER.error("[{}] Could perform search {}", CONTACT_RESOURCE_DESCRIPTION, contactSearchRequest, e);
            return Response.serverError().build();
        }
        return Response.ok(contacts).build();
    }
}
