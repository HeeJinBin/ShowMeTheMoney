package com.example.smtm7.Connection;

public class ResponseCipher {
    private String message;
    private String key;
    private String aes_encrypted_pw;
    private String aes_dscrypted_pw;

    public ResponseCipher(String message, String key, String aes_encrypted_pw, String aes_dscrypted_pw){
        this.message = message;
        this.key = key;
        this.aes_encrypted_pw = aes_encrypted_pw;
        this.aes_dscrypted_pw = aes_dscrypted_pw;
    }

    public String getResult() {
        return message;
    }

    public String getKey() {
        return key;
    }

    public String getAes_encrypted_pw() {
        return aes_encrypted_pw;
    }

    public String getAes_dscrypted_pw() {
        return aes_dscrypted_pw;
    }
}
