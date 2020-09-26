package com.brunogoncalves.phonebook.resource;

import com.brunogoncalves.phonebook.model.PhoneNumber;
import com.brunogoncalves.phonebook.persistence.PhoneNumberPersistence;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/phonebook")
@Singleton
public class PhoneBookResource {

    @Inject
    private PhoneNumberPersistence phoneNumberPersistence;

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public String healthCheck() {
        boolean success = phoneNumberPersistence.create(new PhoneNumber("1","2","2"));
        return "I'm working :)" + success;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@BeanParam PhoneNumber phoneNumber) {
        return "Hello World";
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@BeanParam PhoneNumber phoneNumber) {
        return "Hello World";
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@BeanParam PhoneNumber phoneNumber) {
        return "Hello World";
    }

    @POST
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String search() {
        return "Hello World";
    }
}
