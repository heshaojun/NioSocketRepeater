package org.heath.common;

import org.heath.utils.CertificateUtils;
import org.heath.utils.RSAUtils;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
public class CommonProperties {


    public static volatile byte[] clientAESKey = null;
    public static volatile byte[] serverAESKey = null;
    public static volatile String msgClientId = "";
    public static volatile String msgServerId = "";


    public static final int PACKAGE_SIZE = Integer.valueOf(System.getProperty("package.size", "300"));
    public static final int AUTH_PACKAGE_SIZE = Integer.valueOf(System.getProperty("auth.size", "800"));
    public static final String SERVER_IP = System.getProperty("server.ip", "127.0.0.1");
    public static final int MSG_PORT = Integer.valueOf(System.getProperty("msg.port", "8888"));
    public static final int DOCK_PORT = Integer.valueOf(System.getProperty("dock.port", "9999"));
    public static final String TARGET_IP = System.getProperty("target.ip", "127.0.0.1");
    public static final int TARGET_PORT = Integer.valueOf(System.getProperty("target.port", "8080"));
    public static final byte[] RSA_KEY = CertificateUtils.loadKey(System.getProperty("cert.filename", "rsa.pub"), System.getProperty("cert.password", "12345678"));


}
