package com.brunogoncalves.phonebook.resource;

import com.brunogoncalves.phonebook.domain.Contact;
import com.brunogoncalves.phonebook.domain.ContactSearchRequest;
import com.brunogoncalves.phonebook.persistence.ContactPersistence;
import com.brunogoncalves.phonebook.persistence.ContactPersistenceException;
import com.brunogoncalves.phonebook.persistence.mysql.ContactPersistenceMysql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/contact")
@Singleton
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContactResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactPersistenceMysql.class);

    private static final String CONTACT_RESOURCE_DESCRIPTION = "ContactResource";

    @Inject
    private ContactPersistence contactPersistence;

    @POST
    public Response create(@NotNull @Valid final Contact contact) {
        LOGGER.info("[{}] Create {}", CONTACT_RESOURCE_DESCRIPTION, contact.toString());
        try {
            contactPersistence.create(contact);
        } catch (ContactPersistenceException e) {
            LOGGER.error("[{}] Could not persist {}", CONTACT_RESOURCE_DESCRIPTION, contact.toString(), e);
            return Response.serverError().build();
        }
        return Response.ok(contact).build();
    }

    @PUT
    public Response update(@NotNull @Valid final Contact contact) {
        LOGGER.info("[{}] Update {}", CONTACT_RESOURCE_DESCRIPTION, contact.toString());
        try {
            //TODO: Not Modified?
            contactPersistence.update(contact);
        } catch (ContactPersistenceException e) {
            LOGGER.error("[{}] Could not update {}", CONTACT_RESOURCE_DESCRIPTION, contact.toString(), e);
            return Response.serverError().build();
        }
        return Response.ok(contact).build();
    }

    /**
    @DELETE
    public Response delete(@NotNull @Valid final ContactDeleteRequest deleteRequest) {
        LOGGER.info("[{}] Delete {}", CONTACT_RESOURCE_DESCRIPTION, deleteRequest);
        try {
            contactPersistence.delete(deleteRequest.getPhoneNumber());
        } catch (ContactPersistenceException e) {
            LOGGER.error("[{}] Could not delete {}", CONTACT_RESOURCE_DESCRIPTION, deleteRequest, e);
            return Response.serverError().build();
        }
        return Response.noContent().build();
    }
    **/

    @POST
    @Path("/search")
    public Response search(@NotNull @Valid final ContactSearchRequest contactSearchRequest) {
        return Response.ok("I'm working!").build();
    }
}
