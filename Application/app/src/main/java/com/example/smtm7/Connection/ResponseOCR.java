package com.example.smtm7.Connection;

public class ResponseOCR {
    private String message;
    private String PGname;
    private String date;
    private String price;

    public ResponseOCR(String message, String PGname, String date, String price){
        this.message = message;
        this.PGname = PGname;
        this.date = date;
        this.price = price;
    }

    public String getMessage() {
        return message;
    }

    public String getPGname() {
        return PGname;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }
}
