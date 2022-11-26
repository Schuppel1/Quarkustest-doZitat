package org.schuppel.quarkus.doZitat.resources;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import org.apache.commons.lang3.time.DateUtils;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.schuppel.quarkus.doZitat.model.AppUsers;
import org.schuppel.quarkus.doZitat.model.Login;
import org.schuppel.quarkus.doZitat.repository.UsersRepository;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;


@Path("/users")
@Produces({MediaType.APPLICATION_JSON})
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class UserResources {

    @Inject
    JsonWebToken jwt;

    @Inject
    UsersRepository repository;

    @Inject
    Logger logs;

    @PermitAll
    @Path("/login")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response post(Login login) {

        for (AppUsers user : repository.getAllUsers()) {
            if (user.getName().equals(login.username)) {

                if ( BcryptUtil.matches(
                        login.passwort+"SalzigesSalz",
                        user.getPassword())) {
                    logs.info("Security Token generated for: " +user.getName());
                    if(user.getRole().equals("Admin")){
                        return Response.ok().entity(generateAdminToken(user)).build();
                    } else {
                        return Response.ok().entity(generateUserToken(user)).build();
                    }
                } {
                    logs.info("Security Failed Login attempt for " +user.getName());
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
        }
        logs.info("Security Failed Login attempt not existing user");
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @PermitAll
    @Path("/getADummyLogin")
    @Produces(MediaType.APPLICATION_JSON)
    public Login getDummyLogin() {
        Login dummy = new Login();
        dummy.username = "test";
        dummy.passwort = "testpw";
        return dummy;
    }


    @Transactional
    @PermitAll
    @Path("/getDummyUser")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AppUsers getDummyUser() {
        if (repository.count() == 0) {
            AppUsers newUser = new AppUsers();
            newUser.setName("SvenAdmin");
            newUser.setRole("Admin");
            newUser.setCryptedPassword("password1234SalzigesSalz");
            repository.save(newUser);
        }

        return repository.findByName("SvenAdmin");
    }

    @Transactional
    @PermitAll
    @Path("/register")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response post(AppUsers user) {
        if(!checkUser(user)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (repository.userNameIsFree(user.getName())) {
            AppUsers toSaveUser = new AppUsers();
            toSaveUser.setRole("User");
            toSaveUser.setName(user.getName());
            toSaveUser.setCryptedPassword(user.getPassword());
            repository.save(toSaveUser);
            return Response.ok().build();
        }
        return Response.status(Response.Status.FOUND).build();
    }

    //Hilfsfunktionen
    //testet ob die Eingabe korrekt ist
    private boolean checkUser(AppUsers user) {
        if(user.getPassword().isEmpty()
                ||user.getName().isEmpty()
                || user.getName().isBlank()
                || user.getName().isEmpty()
                || user.getName().isBlank()) {
            return false;
        }
        return true;
    }

    private String generateAdminToken(AppUsers user) {
        Date currentTime = new Date();
        Date expDate = DateUtils.addHours(currentTime, 24);

        return Jwt.issuer("https://example.com/issuer")
                .upn("sven@schuppel.org")
                .groups(new HashSet<>(Arrays.asList("Admin")))
                .expiresIn(300L)
                .claim(Claims.sub.name(), user.getId().toString())
                .claim(Claims.exp.name(), expDate.getTime())
                .sign();
    }

    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }

    private String generateUserToken(AppUsers user) {
        Date currentTime = new Date();
        Date expDate = DateUtils.addHours(currentTime, 24);

        return Jwt.issuer("https://example.com/issuer") // (1)
                .upn("sven@schuppel.org")
                .groups(new HashSet<>(Arrays.asList("User")))
                .expiresIn(300L)
                .claim(Claims.sub.name(), user.getId().toString())
                .claim(Claims.exp.name(), expDate.getTime())
                .sign();
    }

    private String getResponseString(SecurityContext ctx) {
        String name;
        if (ctx.getUserPrincipal() == null) {
            name = "anonymous";
        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
            throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
        } else {
            name = ctx.getUserPrincipal().getName();
        }
        return String.format("hello + %s,"
                        + " isHttps: %s,"
                        + " authScheme: %s,"
                        + " hasJWT: %s",
                name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt());
    }
}