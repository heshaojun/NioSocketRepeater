package org.heath.utils;

import org.apache.commons.codec.binary.Base64;
import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;

import java.util.Hashtable;
import java.util.Map;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/15
 * @Description 消息数据打包和拆包工具
 */
public class MsgPackUtils {
    private static final String FILLER; //填充字符
    private static final String HEADER = "header";//报文头标识
    private static final String SPLIT = "@";//分隔符
    private static final String FILER = "#";//填充符
    private static final String K_V_SPLIT = ":";//键值分隔符


    static {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 1024; i++) {
            builder.append("#");
        }
        FILLER = builder.toString();
    }

    public static byte[] pack(Map<String, String> map, String id) {
        byte[] result = null;
        try {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.append(entry.getKey());
                builder.append(K_V_SPLIT);
                builder.append(entry.getValue());
                builder.append(SPLIT);
            }
            String dataStr = builder.toString();
            byte[] dataBytes = dataStr.getBytes("UTF-8");
            dataStr = Base64.encodeBase64String(dataBytes);
            dataStr = HEADER + SPLIT + CommonProperties.clientId + SPLIT + dataStr + SPLIT;
            int len = dataStr.getBytes().length;
            if (len > CommonProperties.MSG_PACK_SIZE) return null;
            if (len < CommonProperties.MSG_PACK_SIZE) {
                dataStr += FILLER.substring(0, CommonProperties.MSG_PACK_SIZE - len);
                result = dataStr.getBytes("UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String, String> unpack(byte[] data) {
        Hashtable<String, String> result = new Hashtable<>();
        try {
            String context = new String(data);
            if (context.startsWith(HEADER)) {
                String serverId = (context.split(SPLIT))[1];
                String dataStr = (context.split(SPLIT))[2];
                result.put(CommonConst.SERVER_ID, serverId);
                if (dataStr.contains(FILER)) {
                    result = null;
                } else {
                    byte[] dataBytes = Base64.decodeBase64(dataStr);
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
