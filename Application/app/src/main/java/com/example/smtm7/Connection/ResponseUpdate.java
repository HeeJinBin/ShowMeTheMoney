package com.example.smtm7.Connection;

public class ResponseUpdate {
    private String message;
    private int index;

    public ResponseUpdate(String message, int index) {
        this.message = message;
        this.index = index;
    }

    public String getMessage() {
        return message;
    }

    public int getIndex() {
        return index;
    }
}
