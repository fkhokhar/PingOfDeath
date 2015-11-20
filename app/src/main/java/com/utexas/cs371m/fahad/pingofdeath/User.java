package com.utexas.cs371m.fahad.pingofdeath;

import com.shaded.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by fahad on 11/10/15.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class User  implements Serializable {
    private String username;
    private boolean succesfullyPinged;

    public User() {}

    public User(String username, boolean succesfullyPinged) {
        this.username = username;
        this.succesfullyPinged = succesfullyPinged;
    }

    public String getUsername(){
        return username;
    }

    public boolean getSuccessfullyPinged() {
        return succesfullyPinged;
    }
}