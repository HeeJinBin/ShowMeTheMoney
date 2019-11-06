package com.example.smtm7.Connection;

public class ResponseLogin {
    private String message;
    private String nickname;
    private String token;

    public ResponseLogin(String message, String nickname, String token){
        this.message = message;
        this.nickname = nickname;
        this.token = token;
    }

    public String getResult() {
        return message;
    }

    public String getNickname(){
        return nickname;
    }

    public String getToken(){
        return token;
    }
}
