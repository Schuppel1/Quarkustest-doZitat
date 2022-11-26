package org.schuppel.quarkus.doZitat.resources;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.schuppel.quarkus.doZitat.model.Quote;
import org.schuppel.quarkus.doZitat.model.Roles;
import org.schuppel.quarkus.doZitat.repository.QuoteRepository;
import org.schuppel.quarkus.doZitat.utils.TokenGenerator;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Optional;

@Path("/quotes")
@Produces({MediaType.APPLICATION_JSON})
@Consumes(MediaType.APPLICATION_JSON)
public class QuoteResource {

    @Inject
    QuoteRepository repository;

    @Inject
    JsonWebToken jwt;

    @Inject
    Logger logs;

    @GET
    @PermitAll
    public List<Quote> getAllQuotes() {
        logs.info("REST GET All Quotes ");
        TokenGenerator.generateString(List.of(Roles.ADMIN,Roles.AUTHENTICATED));
        return repository.getAllQuotes();
    }

    @GET
    @Path("/count")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public int getNumberOfAllQuotes() {
        logs.info("REST GET COUNT Quotes ");
        return repository.getAllQuotes().size();
    }

    @GET
    @Path("/{id}")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<Quote> getQuote(@PathParam("id") int id) {
        logs.info("REST GET Quote Nr. " +id);
       return Optional.ofNullable(Optional.of(repository.getQuote(id)).orElseThrow(NotFoundException::new));
    }

    @POST
    @RolesAllowed({ "User", "Admin" })
    public Response post(@Context SecurityContext ctx, Quote newQuote) {
        /*  Safe da der Token verschlüsselt und entschlüsselt wurde.
            Somit ist der Token durch die generateUserToken oder generateAdminToken
            entstanden */
        Long userId = Long.parseLong(jwt.getClaim(Claims.sub.name()).toString());

        if(repository.findById(newQuote.id)== null) {
            repository.persist(newQuote);
            return Response.status(201).build();
        } else
            return Response.status(404).build();
    }

    private boolean checkQuote(Quote newQuote) {
        return false;
    }

}