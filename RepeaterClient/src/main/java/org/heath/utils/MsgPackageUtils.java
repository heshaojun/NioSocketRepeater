package org.heath.utils;

import org.heath.common.CommonProperties;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
public class MsgPackageUtils {
    private static final String FILLER;
    private static final String HEADER = "header";
    private static final String SPLIT = "/";


    static {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 1024; i++) {
            builder.append("#");
        }
        FILLER = builder.toString();
    }

    public static byte[] packAuthData(Hashtable<String, String> data) {
        byte[] result = null;
        try {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                builder.append(entry.getKey());
                builder.append(":");
                builder.append(entry.getValue());
                builder.append(SPLIT);
            }
            String dataStr = builder.toString();
            byte[] dataBytes = dataStr.getBytes("UTF-8");
            byte[] encrypted = AESUtils.encrypt(dataBytes, CommonProperties.RSA_KEY);
            dataStr = Base64Utils.encodeToString(encrypted);
            dataStr = HEADER + SPLIT + dataStr + SPLIT;
            if (dataStr.getBytes().length<CommonProperties.PACKAGE_SIZE){

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] packData(Map<String, String> data) {
        return null;
    }

    public static Map<String, Object> packAuthDate(byte[] data) {
        return null;
    }

    public static Map<String, Object> unpackData(byte[] data) {
        return null;
    }

    public static byte[] generateHeartbeat() {
        return null;
    }
}
