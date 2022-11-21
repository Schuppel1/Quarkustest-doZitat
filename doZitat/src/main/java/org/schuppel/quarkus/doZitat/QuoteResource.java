package org.schuppel.quarkus.doZitat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/quotes")
public class QuoteResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Quote> getAllQuotes() {
        return List.of(
                new Quote(1,"ich bin test1", "Prof1", "IT1","User1"),
                new Quote(2,"ich bin test2", "Prof2", "WEBIT","User2"),
                new Quote(3,"ich bin test3", "Prof3", "PHP","User1"),
                new Quote(4,"ich bin test4", "Prof1", "IT2","User1"),
                new Quote(5,"ich bin test5", "Prof2", "WEBIT3","User2")
        );
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public int getNumberOfAllQuotes() {
        return getAllQuotes().size();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<Quote> getQuote(@PathParam("id") int id) {
        return getAllQuotes().stream().filter(quote -> quote.id == id).findFirst();
    }
}