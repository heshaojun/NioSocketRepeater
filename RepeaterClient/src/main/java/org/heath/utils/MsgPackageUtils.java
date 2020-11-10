package org.heath.utils;

import org.heath.common.CommonProperties;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description 消息数据包处理工具
 */
public class MsgPackageUtils {
    private static final String FILLER;
    private static final String HEADER = "header";
    private static final String SPLIT = "@";
    private static final String FILER = "#";
    private static final String K_V_SPLIT = ":";


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
                builder.append(K_V_SPLIT);
                builder.append(entry.getValue());
                builder.append(SPLIT);
            }
            String dataStr = builder.toString();
            byte[] dataBytes = dataStr.getBytes("UTF-8");
            byte[] encrypted = RSAUtils.encrypt(dataBytes, RSAUtils.getPubKey(CommonProperties.RSA_KEY));
            dataStr = Base64Utils.encodeToString(encrypted);
            dataStr = HEADER + SPLIT + dataStr + SPLIT;
            int len = dataStr.getBytes().length;
            if (len > CommonProperties.PACKAGE_SIZE) return null;
            if (len < CommonProperties.PACKAGE_SIZE) {
                dataStr += FILLER.substring(0, CommonProperties.PACKAGE_SIZE - len);
                result = dataStr.getBytes();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] packData(Map<String, String> data) {
        byte[] result = null;
        try {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                builder.append(entry.getKey());
                builder.append(K_V_SPLIT);
                builder.append(entry.getValue());
                builder.append(SPLIT);
            }
            String dataStr = builder.toString();
            byte[] dataBytes = dataStr.getBytes("UTF-8");
            byte[] encrypted = AESUtils.encrypt(dataBytes, CommonProperties.serverAESKey);
            dataStr = Base64Utils.encodeToString(encrypted);
            dataStr = HEADER + SPLIT + dataStr + SPLIT;
            int len = dataStr.getBytes().length;
            if (len > CommonProperties.PACKAGE_SIZE) return null;
            if (len < CommonProperties.PACKAGE_SIZE) {
                dataStr += FILLER.substring(0, CommonProperties.PACKAGE_SIZE - len);
                result = dataStr.getBytes();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Hashtable<String, String> unpackAuthDate(byte[] data) {
        Hashtable<String, String> result = new Hashtable<>();
        try {
            String context = new String(data);
            if (context.startsWith(HEADER)) {
                String dataStr = (context.split(SPLIT))[1];
                if (dataStr.contains(FILER)) {
                    result = null;
                } else {
                    byte[] dataBytes = Base64Utils.decode(dataStr);
                    dataBytes = RSAUtils.decrypt(dataBytes, RSAUtils.getPubKey(CommonProperties.RSA_KEY));
                    String str = new String(dataBytes, "UTF-8");
                    for (String s : str.split(SPLIT)) {
                        if (s.contains(K_V_SPLIT)) {
                            String[] tem = s.split(K_V_SPLIT, 1);
                            result.put(tem[0], tem[1]);
                        }
                    }
                }
            } else {
                result = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public static Hashtable<String, String> unpackData(byte[] data) {

        Hashtable<String, String> result = new Hashtable<>();
        try {
            String context = new String(data);
            if (context.startsWith(HEADER)) {
                String dataStr = (context.split(SPLIT))[1];
                if (dataStr.contains(FILER)) {
                    result = null;
                } else {
                    byte[] dataBytes = Base64Utils.decode(dataStr);
                    dataBytes = AESUtils.decrypt(dataBytes, CommonProperties.clientAESKey);
                    String str = new String(dataBytes, "UTF-8");
                    for (String s : str.split(SPLIT)) {
                        if (s.contains(K_V_SPLIT)) {
                            String[] tem = s.split(K_V_SPLIT, 1);
                            result.put(tem[0], tem[1]);
                        }
                    }
                }
            } else {
                result = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

}
