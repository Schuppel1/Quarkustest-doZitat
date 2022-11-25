package org.schuppel.quarkus.doZitat.utils;

import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;

import java.util.HashSet;
import java.util.List;


public class TokenGenerator {
    //Anleitung siehe https://github.com/quarkusio/quarkus/blob/main/docs/src/main/asciidoc/security-jwt.adoc
    public static String generateString(List<String> roles) {
        String token = Jwt.issuer("https://schuppel.org/issuer")
                .upn("sven@admin.de")
                .groups(new HashSet<String>(roles))
                .expiresIn(300L)
                .claim(Claims.birthdate.name(), "2001-07-13")
                .sign();
        System.out.println(token);
        return token;
    }

    public static void main(String[] args) {
        String token = Jwt.issuer("https://schuppel.org/issuer")
                .upn("sven@admin.de")
                .groups(new HashSet<String>(List.of("User","Admin")))
                .expiresIn(300L)
                .claim(Claims.birthdate.name(), "2001-07-13")
                .sign();
        System.out.println(token);
    }
}
