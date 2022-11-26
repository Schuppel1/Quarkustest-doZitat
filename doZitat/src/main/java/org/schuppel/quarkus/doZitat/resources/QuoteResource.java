package org.schuppel.quarkus.doZitat.resources;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.schuppel.quarkus.doZitat.model.AppUsers;
import org.schuppel.quarkus.doZitat.model.Quote;
import org.schuppel.quarkus.doZitat.model.Roles;
import org.schuppel.quarkus.doZitat.repository.QuoteRepository;
import org.schuppel.quarkus.doZitat.repository.UsersRepository;
import org.schuppel.quarkus.doZitat.utils.TokenGenerator;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Path("/quotes")
@Produces({MediaType.APPLICATION_JSON})
@Consumes(MediaType.APPLICATION_JSON)
public class QuoteResource {

    @Inject
    QuoteRepository repository;

    @Inject
    UsersRepository userRepository;

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
    @Transactional
    public Response post(@Context SecurityContext ctx, Quote newQuote) {
        /*  Safe da der Token verschlüsselt und entschlüsselt wurde.
            Somit ist der Token durch die generateUserToken oder generateAdminToken
            entstanden */
        Long userId = Long.parseLong(jwt.getClaim(Claims.sub.name()).toString());

        if(!checkQuote(newQuote)){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // Falls ein Quote geupdatet werden soll, muss put verwendet werden.
        if(newQuote.id==null ||newQuote.id==0L ) {
            newQuote.creatorId=userId;
            newQuote.created = Instant.now();
            repository.persist(newQuote);
            return Response.status(201).build();
        } else {
            return Response.status(Response.Status.FOUND).build();
        }
    }

    @PUT
    @RolesAllowed({ "User", "Admin" })
    @Transactional
    @Path("/{id}")
    public Response put(@PathParam("id") int id, @Context SecurityContext ctx, Quote newQuote) {

        Long quoteId = (long) id;
        Long userId = Long.parseLong(jwt.getClaim(Claims.sub.name()).toString());

        Quote persQuote = repository.findById(quoteId);

        if(persQuote==null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(!persQuote.creatorId.equals(userId)) {
            if(!jwt.getGroups().contains("Admin")) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
        if(!newQuote.quote.isEmpty() && !newQuote.quote.isBlank()) {
            persQuote.quote= newQuote.quote;
        }

        if(!newQuote.profName.isEmpty() && !newQuote.profName.isBlank()) {
            persQuote.profName= newQuote.profName;
        }

        if(!newQuote.course.isEmpty() && !newQuote.course.isBlank()) {
            persQuote.course= newQuote.course;
        }
        repository.persist(persQuote);
        return  Response.status(Response.Status.OK).build();
    }

    @DELETE
    @RolesAllowed({ "User", "Admin" })
    @Transactional
    @Path("/{id}")
    public Response delete(@PathParam("id") int id, @Context SecurityContext ctx) {
        Quote persQuote = repository.findById((long) id);
        Long userId = Long.parseLong(jwt.getClaim(Claims.sub.name()).toString());

        if(persQuote == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(jwt.getGroups().contains("Admin")) {
            repository.deleteById((long) id);
            return Response.status(Response.Status.OK).build();
        }

        if(userId == persQuote.creatorId) {
            repository.deleteById((long) id);
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    private boolean checkQuote(Quote newQuote) {
        if(newQuote.quote.isEmpty()
                || newQuote.quote.isBlank()
                || newQuote.course.isBlank()
                || newQuote.course.isEmpty()
                || newQuote.profName.isBlank()
                || newQuote.profName.isEmpty()) {
            return false;
        }
        return true;
    }

}