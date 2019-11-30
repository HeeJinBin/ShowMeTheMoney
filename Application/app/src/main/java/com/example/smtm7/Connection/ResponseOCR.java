package com.example.smtm7.Connection;

public class ResponseOCR {
    private String message;
    private String PGname;
    private String date;
    private int price;

    public ResponseOCR(String message, String PGname, String date, int price){
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

    public int getPrice() {
        return price;
    }
}
