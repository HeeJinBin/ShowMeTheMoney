package com.example.smtm7.InitialSetting;

public class EmailItem {
    private String email;
    private String pw;
    private int indexs;

    public EmailItem(String email, String pw, int indexs) {
        this.email = email;
        this.pw = pw;
        this.indexs = indexs;
    }

    public String getEmail() {
        return email;
    }

    public String getPw() {
        return pw;
    }

    public int getIndexs() {
        return indexs;
    }
}
