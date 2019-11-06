package com.example.smtm7.Connection;

public class ResponseSignin {
    private String message;

    public ResponseSignin(String message){
        this.message = message;
    }

    public String getResult() {
        return message;
    }
}
