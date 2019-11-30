package com.example.smtm7.DataBase;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
    public static String Encode(String key, String pw, String token) throws java.io.UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] textPw = pw.getBytes("UTF-8");
        String iv = "1234567890123456";
        byte[] textIv = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        AlgorithmParameterSpec ivspec = new IvParameterSpec(textIv);

        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"),"AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivspec);
        return Base64.encodeToString(cipher.doFinal(textPw),0);
    }
}
