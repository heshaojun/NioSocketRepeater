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


    public static final ArrayBlockingQueue<byte[]> SERVER_MSG_QUEUE = new ArrayBlockingQueue<byte[]>(Integer.valueOf(System.getProperty("server.msg.queue", "100")));
    public static final ArrayBlockingQueue<byte[]> CLIENT_MSG_QUEUE = new ArrayBlockingQueue<byte[]>(Integer.valueOf(System.getProperty("client.msg.queue", "100")));
    public static final ArrayBlockingQueue<String> DOCK_MSG_QUEUE = new ArrayBlockingQueue<String>(Integer.valueOf(System.getProperty("server.msg.queue", "100")));
    public static final int REPEAT_BUFF_SIZE = Integer.valueOf(System.getProperty("repeat.buff.size", "" + (5*1024 * 1024)));


    public static final Hashtable<SocketChannel, ChannelInfo> MAPPED_CHANNEL = new Hashtable<>(300);

    @Data
    public static class ChannelInfo {
        private SocketChannel self;
        private SocketChannel mapped;
        private ByteBuffer buffer;
        private long refreshTime;

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
