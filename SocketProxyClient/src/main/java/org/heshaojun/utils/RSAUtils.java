package org.heshaojun.utils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author heshaojun
 * @date 2020/11/10
 * @description TODO
 */
public class RSAUtils {
    public static final String PUBLIC_KEY = "pub";
    public static final String PRIVATE_KEY = "pri";

    public static byte[] encrypt(byte[] data, Key key) {
        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            result = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] decrypt(byte[] encryptedBytes, Key key) {
        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            result = cipher.doFinal(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String, byte[]> generateKeys() {
        Map<String, byte[]> result = new Hashtable<>();
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            result.put(PUBLIC_KEY, rsaPublicKey.getEncoded());
            result.put(PRIVATE_KEY, rsaPrivateKey.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static PrivateKey getPriKey(byte[] keyBytes) {
        PrivateKey privateKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static PublicKey getPubKey(byte[] keyBytes) {
        PublicKey publicKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }
}
