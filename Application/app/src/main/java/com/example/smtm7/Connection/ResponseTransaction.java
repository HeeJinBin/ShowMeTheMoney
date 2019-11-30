package com.example.smtm7.Connection;

public class ResponseTransaction {
    private int myIndex;
    private String PGname;
    private String date;
    private String purchasing_office = null;
    private String purchasing_item;
    private int price;

    public ResponseTransaction(String PGname, String date, String purchasing_item, int price) {
        this.PGname = PGname;
        this.date = date;
        this.purchasing_item = purchasing_item;
        this.price = price;
    }

    public ResponseTransaction(String PGname, String date, String purchasing_office, String purchasing_item, int price) {
        this.PGname = PGname;
        this.date = date;
        this.purchasing_office = purchasing_office;
        this.purchasing_item = purchasing_item;
        this.price = price;
    }

    public String getPGname() {
        return PGname;
    }

    public String getDate() {
        return date;
    }

    public String getPurchasing_office() {
        return purchasing_office;
    }

    public String getPurchasing_item() {
        return purchasing_item;
    }

    public int getPrice() {
        return price;
    }
}
