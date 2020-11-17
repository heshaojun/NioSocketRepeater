package org.heath.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;
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
    public static final Hashtable<SocketChannel, MsgClientInfo> MSG_CLIENT_INFO_MAP = new Hashtable<>();


    @Data
    public static class MsgClientInfo {
        private SocketChannel channel;
        private String id;
        private byte[] key;
        private long refreshTime;
        private ByteBuffer buffer;

        public MsgClientInfo(SocketChannel channel, String id, byte[] key, long refreshTime) {
            this.channel = channel;
            this.id = id;
            this.key = key;
            this.refreshTime = refreshTime;
            this.buffer = ByteBuffer.allocateDirect(CommonProperties.MSG_PACK_SIZE);
        }
    }
}
