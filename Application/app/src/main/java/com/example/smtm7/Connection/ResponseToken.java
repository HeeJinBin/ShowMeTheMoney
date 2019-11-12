package com.example.smtm7.Connection;

public class ResponseToken {
    private String refresh;
    private String access;

    public ResponseToken(String refresh, String access){
        this.refresh = refresh;
        this.access = access;
    }

    public String getRefresh() {
        return refresh;
    }

    public String getAccess() {
        return access;
    }
}
