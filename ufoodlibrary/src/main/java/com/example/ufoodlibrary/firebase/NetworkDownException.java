package com.example.ufoodlibrary.firebase;

/**
 * Created by Alfonso on 22-May-16.
 */
public class NetworkDownException extends Exception{

    @Override
    public String getMessage() {
        return "Network down, check your connection";
    }
}
