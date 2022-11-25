package org.schuppel.quarkus.doZitat.resources;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.schuppel.quarkus.doZitat.model.AppUsers;
import org.schuppel.quarkus.doZitat.model.Login;
import org.schuppel.quarkus.doZitat.model.Quote;
import org.schuppel.quarkus.doZitat.repository.QuoteRepository;
import org.schuppel.quarkus.doZitat.repository.UsersRepository;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Path("/users")
@Produces({MediaType.APPLICATION_JSON})
@Consumes(MediaType.APPLICATION_JSON)
public class UserResources {


    @Inject
    JsonWebToken jwt;

    UsersRepository repository;

    @Inject
    Logger logs;

    @Path("/login")
    @POST
    public JsonWebToken post(Login login) throws NoSuchAlgorithmException {
        for (AppUsers user : repository.getAllUsers()) {
            if (user.getName().equals(login.username)) {

                MessageDigest md = MessageDigest.getInstance("SHA3-512");
                byte[] result = md.digest((login.passwort + "dhbwmosbach").getBytes(StandardCharsets.UTF_8));
                String hash = new String(result, StandardCharsets.UTF_8);

                if (user.getPassword().equals(hash)) {
                    logs.info("Security Token generated for: " +user.getName());
                    //todo;
                }
            }
        }
        return null;
    }

    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }
}