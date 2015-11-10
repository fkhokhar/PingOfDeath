package com.utexas.cs371m.fahad.pingofdeath;

/**
 * Created by fahad on 11/10/15.
 */
public class User {
    private boolean succesfullyPinged;

    public User() {}

    public User(boolean succesfullyPinged) {
        this.succesfullyPinged = succesfullyPinged;
    }

    public boolean getSuccessfullyPinged() {
        return succesfullyPinged;
    }
}