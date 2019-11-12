package com.example.smtm7.Connection;

public class ResponseLogin {
    private String message;
    private String nickname;

    public ResponseLogin(String message, String nickname){
        this.message = message;
        this.nickname = nickname;
    }

    public String getResult() {
        return message;
    }

    public String getNickname(){
        return nickname;
    }
}
