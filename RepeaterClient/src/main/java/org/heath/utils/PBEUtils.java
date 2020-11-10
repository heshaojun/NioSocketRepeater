package org.heath.utils;

import org.apache.commons.codec.digest.Md5Crypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
public class PBEUtils {
    public static byte[] encrypt(byte[] originalData, String passwd) {
        byte[] result = null;
        try {
            PBEKeySpec keySpec = new PBEKeySpec(passwd.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            Key key = keyFactory.generateSecret(keySpec);
            byte[] salt = getSalt(passwd);
            PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, 100);
            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            result = cipher.doFinal(originalData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] decrypt(byte[] encryptData, String passwd) {
        byte[] result = null;
        try {
            PBEKeySpec keySpec = new PBEKeySpec(passwd.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            Key key = keyFactory.generateSecret(keySpec);
            byte[] salt = getSalt(passwd);
            PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, 100);
            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
            result = cipher.doFinal(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static byte[] getSalt(String passwd) {
        final String filer = "********";
        if (passwd.length() < 8) {
            passwd += filer.substring(0, 8 - passwd.length());
        }
        byte[] key = Base64Utils.encode(passwd.getBytes());
        byte[] salt = Arrays.copyOf(key, 8);
        return salt;
    }
}
