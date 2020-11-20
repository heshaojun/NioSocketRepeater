package org.heshaojun.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * @author heshaojun
 * @date 2020/11/10
 * @description TODO
 */
public class AESUtils {
    public static byte[] encrypt(byte[] originalData, byte[] keyBytes) {
        byte[] result = null;
        try {
            Key key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // 确定算法
            cipher.init(Cipher.ENCRYPT_MODE, key);    // 确定密钥
            result = cipher.doFinal(originalData);  // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] decrypt(byte[] encryptedData, byte[] keyBytes) {
        byte[] result = null;
        try {
            Key key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // 确定算法
            cipher.init(Cipher.DECRYPT_MODE, key);    // 确定密钥
            result = cipher.doFinal(encryptedData);  // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] generateKey() {
        byte[] result = null;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            result = keyGenerator.generateKey().getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
