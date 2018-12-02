package com.uottawa.olympus.olympusservices;

import android.os.Message;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.lang.Math;

public class PasswordEncryption {
    private static final MessageDigest MESSAGE_DIGEST;
    private static final char[] POSSIBLE_CHARS;
    private static final SecureRandom random;

    static{
        MessageDigest messageDigest;
        try{
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e){
            messageDigest = null;
        }
        MESSAGE_DIGEST = messageDigest;

        POSSIBLE_CHARS = new char[94];
        for (int i = 33; i<127; i++){
            POSSIBLE_CHARS[i-33] = (char)i;
        }

        random = new SecureRandom();
    }

    public static String encrypt(String password, String salt){
        password = salt + password;

        byte[] passwordBytes = password.getBytes();
        byte[] hashedPassword;

        MESSAGE_DIGEST.reset();
        MESSAGE_DIGEST.update(passwordBytes);
        hashedPassword = MESSAGE_DIGEST.digest();


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hashedPassword.length; i++) {
            if ((hashedPassword[i] & 0xff) < 0x10) {
                sb.append("0");
            }

            sb.append(Long.toString(hashedPassword[i] & 0xff, 16));
        }
        return sb.toString();
    }

    public static String generateSalt(){
        long stringID = Math.abs(random.nextLong());
        String salt = "";

        while(stringID > 0){
            //the least significant digit is added to nonce string first
            int index = (int)(stringID%94);
            salt += POSSIBLE_CHARS[index];
            stringID /= 94;
        }
        return salt;
    }

    public static boolean slowEquals(String one, String two){
        byte[] first = one.getBytes();
        byte[] second = two.getBytes();

        int length = first.length > second.length ? second.length : first.length;
        boolean same = true;
        for (int i = 0; i<length; i++){
            if (first[i] != second[i]){
                same = false;
            }
        }
        return same;
    }
}
