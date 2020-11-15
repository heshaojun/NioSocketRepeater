package org.heath.common;

import lombok.Data;
import org.heath.utils.CertificateUtils;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
public class CommonProperties {


    public static final Hashtable<SocketChannel, ClientInfoMap> MSG_CLIENT_INFO_MAP = new Hashtable<>();


    public static final ArrayBlockingQueue<byte[]> SERVER_MSG_QUEUE = new ArrayBlockingQueue<byte[]>(Integer.valueOf(System.getProperty("server.msg.queue", "100")));
    public static final ArrayBlockingQueue<byte[]> CLIENT_MSG_QUEUE = new ArrayBlockingQueue<byte[]>(Integer.valueOf(System.getProperty("client.msg.queue", "100")));

    public static final int PACKAGE_SIZE = Integer.valueOf(System.getProperty("package.size", "300"));
    public static final int AUTH_PACKAGE_SIZE = Integer.valueOf(System.getProperty("auth.size", "800"));

    public static final int MSG_PORT = Integer.valueOf(System.getProperty("msg.port", "8888"));
    public static final int DOCK_PORT = Integer.valueOf(System.getProperty("dock.port", "9999"));
    public static final int FRONT_PORT = Integer.valueOf(System.getProperty("front.port", "80"));
    public static final byte[] RSA_KEY = CertificateUtils.loadKey(System.getProperty("cert.filename", "rsa.pri"), System.getProperty("cert.password", "12345678"));


    @Data
    public static class ClientInfoMap {
        private String serverId;
        private String clientId;
        private byte[] serverKey;
        private byte[] clientKey;
        private SocketChannel channel;
        private long refreshTime;
        private ByteBuffer buffer;

        public ClientInfoMap(String serverId, String clientId, byte[] serverKey, byte[] clientKey, SocketChannel channel) {
            this.serverId = serverId;
            this.clientId = clientId;
            this.clientKey = clientKey;
            this.serverKey = serverKey;
            this.channel = channel;
            this.refreshTime = new Date().getTime();
            this.buffer = ByteBuffer.allocateDirect(PACKAGE_SIZE);
        }
    }
}
