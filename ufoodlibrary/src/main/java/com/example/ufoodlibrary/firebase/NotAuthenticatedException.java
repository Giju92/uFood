package com.example.ufoodlibrary.firebase;

/**
 * Created by Alfonso on 21-May-16.
 */
public class NotAuthenticatedException extends Exception {

    @Override
    public String getMessage() {
        return "Autentication required to do this action !";
    }
}
