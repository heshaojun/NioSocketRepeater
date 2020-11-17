package org.heath.common;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author heshaojun
 * @date 2020/11/11
 * @description TODO
 */
public class CommonConst {
    public static final String TYPE = "type";
    public static final String TOKEN_TYPE = "token_type";
    public static final String TOKEN = "token";
    public static final String DOCK_TYPE = "dock_type";
    public static final String DOCK = "dock";
    public static final String CMD_TYPE = "CMD_type";
    public static final String CMD = "CMD";
    public static final String KEY_TYPE = "key_type";
    public static final String KEY = "key";
    public static final String HEARTBEAT_TYPE = "heartbeat_type";
    public static final String HEARTBEAT = "heartbeat";

    public static final String SERVER_ID = "server_id";
    public static final String CLIENT_ID = "client_id";


    public static final ArrayBlockingQueue<byte[]> SERVER_MSG_QUEUE = new ArrayBlockingQueue<byte[]>(Integer.valueOf(System.getProperty("server.msg.queue", "100")));
    public static final ArrayBlockingQueue<byte[]> CLIENT_MSG_QUEUE = new ArrayBlockingQueue<byte[]>(Integer.valueOf(System.getProperty("client.msg.queue", "100")));
}
