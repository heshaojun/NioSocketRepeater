package org.heath.common;

import org.heath.utils.CertificateUtils;

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


    public static final ArrayBlockingQueue<byte[]> SERVER_MSG_QUEUE = new ArrayBlockingQueue<byte[]>(Integer.valueOf(System.getProperty("server.msg.queue", "100")));
    public static final ArrayBlockingQueue<byte[]> CLIENT_MSG_QUEUE = new ArrayBlockingQueue<byte[]>(Integer.valueOf(System.getProperty("client.msg.queue", "100")));

    public static final int PACKAGE_SIZE = Integer.valueOf(System.getProperty("package.size", "300"));
    public static final int AUTH_PACKAGE_SIZE = Integer.valueOf(System.getProperty("auth.size", "800"));

    public static final int MSG_PORT = Integer.valueOf(System.getProperty("msg.port", "8888"));
    public static final int DOCK_PORT = Integer.valueOf(System.getProperty("dock.port", "9999"));
    public static final int FRONT_PORT = Integer.valueOf(System.getProperty("front.port", "80"));
    public static final byte[] RSA_KEY = CertificateUtils.loadKey(System.getProperty("cert.filename", "rsa.pri"), System.getProperty("cert.password", "12345678"));


}
