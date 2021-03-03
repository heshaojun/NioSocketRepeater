package org.heshaojun.common;

import lombok.Data;
import sun.nio.ch.DirectBuffer;

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
    public static final int REPEAT_BUFF_SIZE = Integer.valueOf(System.getProperty("repeat.buff.size", "" + (5 * 1024 * 1024)));
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

        public void refresh() {
            this.refreshTime = new Date().getTime();
        }

        public MsgClientInfo(SocketChannel channel, String id, byte[] serverKey, byte[] clientKey) {
            this.channel = channel;
            this.id = id;
            this.serverKey = serverKey;
            this.clientKey = clientKey;
            this.refreshTime = new Date().getTime();
            this.buffer = ByteBuffer.allocateDirect(CommonProperties.PACK_SIZE);
        }

        public void destroy() {
            try {
                channel.close();
            } catch (Exception e) {
            }
            try {
                ((DirectBuffer) buffer).cleaner().clean();
            } catch (Exception e) {
            }
            try {
                MSG_CLIENT_INFO_MAP.remove(channel);
            } catch (Exception e) {
            }
        }

        public boolean ifTimeout(long timeout) {
            if (new Date().getTime() - refreshTime > timeout) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static final Hashtable<String, CachedChannelInfo> CACHED_CHANNEL_INFO_MAP = new Hashtable<>(100);

    @Data
    public static class CachedChannelInfo {
        private SocketChannel channel;
        private String id;
        private long refreshTime;

        public CachedChannelInfo(SocketChannel channel, String id) {
            this.channel = channel;
            this.id = id;
            this.refreshTime = new Date().getTime();
        }

        public boolean ifTimeout(long timeout) {
            if (new Date().getTime() - refreshTime > timeout) {
                return true;
            } else {
                return false;
            }
        }

        public void destroy() {
            try {
                channel.close();
            } catch (Exception e) {
            }
            try {
                CACHED_CHANNEL_INFO_MAP.remove(id);
            } catch (Exception e) {
            }
        }
    }

    public static final Hashtable<SocketChannel, ChannelInfo> MAPPED_CHANNEL = new Hashtable<>(300);

    @Data
    public static class ChannelInfo {
        private SocketChannel self;
        private SocketChannel mapped;
        private ByteBuffer buffer;
        private long refreshTime;

        private volatile boolean lock = false;

        public boolean lock() {
            if (lock) {
                return false;
            } else {
                lock = true;
                return true;
            }
        }

        public void unlock() {
            lock = false;
        }


        public ChannelInfo(SocketChannel self, SocketChannel mapped) {
            this.self = self;
            this.mapped = mapped;
            this.buffer = ByteBuffer.allocateDirect(REPEAT_BUFF_SIZE);
            this.refreshTime = new Date().getTime();
        }

        public void refresh() {
            this.refreshTime = new Date().getTime();
        }

        public boolean ifTimeout(long timeout) {
            try {
                if (new Date().getTime() - refreshTime > timeout && new Date().getTime() - MAPPED_CHANNEL.get(mapped).getRefreshTime() > timeout) {
                    return true;
                }
            } catch (Exception e) {
            }
            return false;
        }

        public void destroy() {
            try {
                MAPPED_CHANNEL.remove(self);
            } catch (Exception e) {
            }
            try {
                self.close();
            } catch (Exception e) {
            }
            try {
                ((DirectBuffer) buffer).cleaner().clean();
            } catch (Exception e) {
            }
            try {
                MAPPED_CHANNEL.remove(mapped).destroy();
            } catch (Exception e) {
            }
        }
    }
}
