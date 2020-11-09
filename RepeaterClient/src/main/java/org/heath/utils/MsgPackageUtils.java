package org.heath.utils;

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

    public static byte[] packDataWithEncrypt(byte[] data, int type) {
        return null;
    }

    public static byte[] packData(byte[] data, int type) {
        return null;
    }

    public static Map<String, Object> unpackDataWithEncrypt(byte[] data) {
        return null;
    }

    public static Map<String, Object> unpackData(byte[] data) {
        return null;
    }

    public static byte[] generateHeartbeat() {
        return null;
    }
}
