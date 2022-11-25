package org.schuppel.quarkus.doZitat.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login {
    public String username;

    //in Sha3 + salt "dhbwmosbach"
    public String passwort;

    public void setPasswortWithCleartext(String cleartext) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA3-512");
        byte[] result= md.digest((cleartext+"dhbwmosbach").getBytes(StandardCharsets.UTF_8));
        this.passwort = new String(result,StandardCharsets.UTF_8);
    }
}
