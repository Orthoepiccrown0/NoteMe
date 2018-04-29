package com.epiccrown.me.note.noteme.Helpers;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by Epiccrown on 29.04.2018.
 */

public class MD5helper {

    public static String getMD5string(String plaintext){
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(plaintext.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            String hashtext = bigInt.toString(16);
            while(hashtext.length() < 32 ){
                hashtext = "0"+hashtext;
            }
            return hashtext;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

}
