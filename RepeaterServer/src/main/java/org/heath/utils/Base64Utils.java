package org.heath.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * @author heshaojun
 * @date 2020/11/10
 * @description TODO
 */
public class Base64Utils {
    public static byte[] encode(byte[] data) {
        return Base64.encodeBase64(data);
    }

    public static String encodeToString(byte[] data) {
        return Base64.encodeBase64String(data);
    }

    public static byte[] decode(byte[] bas64Data) {
        return Base64.decodeBase64(bas64Data);
    }

    public static byte[] decode(String base64String) {
        return Base64.decodeBase64(base64String);
    }
}
