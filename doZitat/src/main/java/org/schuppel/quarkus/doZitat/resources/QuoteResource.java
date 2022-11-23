package org.schuppel.quarkus.doZitat.resources;

import org.jboss.logging.Logger;
import org.schuppel.quarkus.doZitat.model.Quote;
import org.schuppel.quarkus.doZitat.repository.QuoteRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/quotes")
public class QuoteResource {

    @Inject
    QuoteRepository repository;

    @Inject
    Logger logs;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Quote> getAllQuotes() {
        logs.info("REST GET All Quotes ");
        return repository.getAllQuotes();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public int getNumberOfAllQuotes() {
        logs.info("REST GET COUNT Quotes ");
        return repository.getAllQuotes().size();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<Quote> getQuote(@PathParam("id") int id) {
        logs.info("REST GET Quote Nr. " +id);
       return repository.getQuote(id);
    }
}