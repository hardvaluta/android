package com.android1337;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by victor on 2016-10-06.
 * CURRENTLY UNUSED
 */

public class MD5 {

    public static String hash(String password){

        String hashedPassword = "";
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            //Add password bytes to digest
            md.update(password.getBytes());

            //Get the hash's bytes
            byte[] bytes = md.digest();

            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();

            for(int i=0; i< bytes.length ;i++)
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));

            //Get complete hashed password in hex format
            hashedPassword = sb.toString();

        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }

        return hashedPassword;
    }
}
