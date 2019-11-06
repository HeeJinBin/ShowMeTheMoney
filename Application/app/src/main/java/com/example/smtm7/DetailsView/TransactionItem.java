package com.example.smtm7.DetailsView;

public class TransactionItem {
    private String item;
    private String date;
    private String office = null;
    private String pg;
    private String price;

    public TransactionItem(String pg, String date, String item, String price) {
        this.item = item;
        this.date = date;
        this.pg = pg;
        this.price = price;
    }

    public TransactionItem(String pg, String date, String office, String item, String price) {
        this.item = item;
        this.date = date;
        this.office = office;
        this.pg = pg;
        this.price = price;
    }

    public String getItem() {
        return item;
    }

    public String getDate(){
        return date;
    }

    public String getOffice() {
        return office;
    }

    public String getPg() {
        return pg;
    }

    public String getPrice() {
        return price;
    }
}
