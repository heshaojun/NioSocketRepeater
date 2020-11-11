package org.heath.common;

import java.nio.channels.SocketChannel;
import java.util.Hashtable;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
public class CommonProperties {


    public static final Hashtable<String, byte[]> CLIENT_AES_KEY_MAP = new Hashtable<>();
    public static final Hashtable<String, byte[]> SERVER_AES_KEY_MAP = new Hashtable<>();
    public static final Hashtable<SocketChannel, String> MSG_CLIENT_ID_MAP = new Hashtable<>();


    public static final int KEY_TYPE = 1 << 1;
    public static final int HEART_BEAT_TYPE = 1 << 2;
    public static final int DOCK_TYPE = 1 << 3;
    public static final int CMD_TYPE = 1 << 4;

    public static final int ENCRYPTED = 1 << 1;
    public static final int UNENCRYPTED = 1 << 2;

    public static final ArrayBlockingQueue<byte[]> SERVER_MSG_QUEUE = new ArrayBlockingQueue<byte[]>(Integer.valueOf(System.getProperty("server.msg.queue", "100")));
    public static final ArrayBlockingQueue<byte[]> CLIENT_MSG_QUEUE = new ArrayBlockingQueue<byte[]>(Integer.valueOf(System.getProperty("client.msg.queue", "100")));

    public static final int PACKAGE_SIZE = Integer.valueOf(System.getProperty("package.size", "500"));
    public static final String SERVER_IP = System.getProperty("server.ip", "127.0.0.1");
    public static final int MSG_PORT = Integer.valueOf(System.getProperty("msg.port", "8888"));
    public static final int DOCK_PORT = Integer.valueOf(System.getProperty("dock.port", "9999"));
    public static final String TARGET_IP = System.getProperty("target.ip", "127.0.0.1");
    public static final int TARGET_PORT = Integer.valueOf(System.getProperty("target.port", "8080"));
    public static final byte[] RSA_KEY = CertificateUtils.loadKey(System.getProperty("cert.filename", "rsa.pri"), System.getProperty("cert.password", "12345678"));


}
