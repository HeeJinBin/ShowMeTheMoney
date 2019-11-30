package com.example.smtm7.DataBase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class KeyGeneration {
    private String key;

    public KeyGeneration(String token) {
        key = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(token.substring(7).getBytes());
            byte[] hash = digest.digest();

            StringBuffer buffer = new StringBuffer();
            for(int i=0;i<hash.length;i++){
                buffer.append(Integer.toString((hash[i]&0xff) + 0x100, 16).substring(1));
            }
            key = buffer.toString();
            key = getKey().toLowerCase();
            key = getKey().substring(0,32);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            key = null;
        }
    }

    public String getKey() {
        return key;
    }
}
