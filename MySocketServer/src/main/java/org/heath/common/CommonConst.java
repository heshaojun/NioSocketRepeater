package org.heath.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.naming.ldap.SortKey;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
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

    public static final String AUTH_ID = "auth_id";


    public static final ArrayBlockingQueue<String> DOCK_MSG_QUEUE = new ArrayBlockingQueue<String>(Integer.valueOf(System.getProperty("dock.msg.queue", "100")));
    public static final ArrayBlockingQueue<byte[]> CLIENT_MSG_QUEUE = new ArrayBlockingQueue<byte[]>(Integer.valueOf(System.getProperty("client.msg.queue", "100")));
    //消息通道映射表
    public static final Hashtable<SocketChannel, MsgClientInfo> MSG_CLIENT_INFO_MAP = new Hashtable<>(20);


    @Data
    public static class MsgClientInfo {
        private SocketChannel channel;
        private String id;
        private byte[] serverKey;
        private byte[] clientKey;
        private long refreshTime;
        private ByteBuffer buffer;

        public MsgClientInfo(SocketChannel channel, String id, byte[] serverKey, byte[] clientKey) {
            this.channel = channel;
            this.id = id;
            this.serverKey = serverKey;
            this.clientKey = clientKey;
            this.refreshTime = new Date().getTime();
            this.buffer = ByteBuffer.allocateDirect(CommonProperties.PACK_SIZE);
        }
    }

    public static final Hashtable<String, CachedChannelInfo> CACHED_CHANNEL_INFO_MAP = new Hashtable<>(100);

    @Data
    public static class CachedChannelInfo {
        private SocketChannel channel;
        private long refreshTime;

        public CachedChannelInfo(SocketChannel channel) {
            this.channel = channel;
            this.refreshTime = new Date().getTime();
        }
    }
}
