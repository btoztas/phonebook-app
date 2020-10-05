package com.brunogoncalves.phonebook.backend.resource;

import com.brunogoncalves.phonebook.backend.domain.Contact;
import com.brunogoncalves.phonebook.backend.domain.ContactData;
import com.brunogoncalves.phonebook.backend.domain.ContactSearchRequest;
import com.brunogoncalves.phonebook.backend.storage.ContactStorage;
import com.brunogoncalves.phonebook.backend.storage.exception.*;
import com.brunogoncalves.phonebook.backend.storage.mysql.ContactStorageMysql;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: Better error handling for the Storage Statements
/**
 * The Backend resource that is responsible to handle all the REST Requests for Contacts
 */
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
    public Response create(@NotNull @Valid final ContactData contactData) {
        LOGGER.info("[{}] Create {}", CONTACT_RESOURCE_DESCRIPTION, contactData);
        Contact contact;
        try {
            contact = contactStorage.create(contactData);
        } catch (ContactStorageException | CouldNotInsertContactException e) {
            LOGGER.error("[{}] Could not create {}", CONTACT_RESOURCE_DESCRIPTION, contactData, e);
            return Response.serverError().build();
        }
        LOGGER.debug("[{}] Contact {} was created", CONTACT_RESOURCE_DESCRIPTION, contact);
        return Response.ok(contact).build();
    }

    @GET
    @Path("/{contactId}")
    public Response get(@PathParam("contactId") final Integer contactId) {
        LOGGER.info("[{}] Get contact wih id {}", CONTACT_RESOURCE_DESCRIPTION, contactId);
        Contact contact;
        try {
            contact = contactStorage.get(contactId);
        } catch (ContactStorageException | CouldNotGetContactException e) {
            LOGGER.error("[{}] Could not get contact with Id {}", CONTACT_RESOURCE_DESCRIPTION, contactId, e);
            return Response.serverError().build();
        }
        LOGGER.debug("[{}] Contact with id {} with data {} will be returned", CONTACT_RESOURCE_DESCRIPTION,contactId, contact);
        return Response.ok(contact).build();
    }

    @PUT
    @Path("/{contactId}")
    public Response update(@NotNull @Valid final ContactData contactData, @PathParam("contactId") final Integer contactId) {
        LOGGER.info("[{}] Update {} with contact data {}", CONTACT_RESOURCE_DESCRIPTION, contactId, contactData);
        try {
            contactStorage.update(contactData, contactId);
        } catch (ContactStorageException | CouldNotUpdateContactException e) {
            LOGGER.error("[{}] Could not perform update {}", CONTACT_RESOURCE_DESCRIPTION, contactData, e);
            return Response.serverError().build();
        }
        LOGGER.debug("[{}] Updated {} with {}", CONTACT_RESOURCE_DESCRIPTION, contactId, contactData);
        return Response.noContent().build();
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
        LOGGER.debug("[{}] Search {} returned results: {}", CONTACT_RESOURCE_DESCRIPTION, contactSearchRequest, contacts);
        return Response.ok(contacts).build();
    }

    @DELETE
    @Path("/{contactId}")
    public Response delete(@PathParam("contactId") Integer contactId) {
        LOGGER.info("[{}] Delete Request for {}", CONTACT_RESOURCE_DESCRIPTION, contactId);
        try {
            contactStorage.delete(contactId);
        } catch (ContactStorageException | CouldNotDeleteContactException e) {
            LOGGER.error("[{}] Could not perform delete request {}", CONTACT_RESOURCE_DESCRIPTION, e);
            return Response.serverError().build();
        }
        LOGGER.debug("[{}] Deleted {}", CONTACT_RESOURCE_DESCRIPTION, contactId);
        return Response.noContent().build();
    }
}
