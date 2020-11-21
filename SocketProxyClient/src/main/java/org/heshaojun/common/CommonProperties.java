package org.heshaojun.common;

import org.heshaojun.utils.CertificateUtils;
import org.heshaojun.utils.RSAUtils;

import java.security.Key;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public class CommonProperties {
    public static final int PACK_SIZE = Integer.valueOf(System.getProperty("pack.size", "200"));
    public static final int AUTH_PACK_SIZE = Integer.valueOf(System.getProperty("auth.pack.size", "500"));
    public static volatile String authId = "authId";
    public static volatile String token = "token";
    public static final String SERVER_IP = System.getProperty("server.ip", "39.102.81.87");
    //public static final String TARGET_IP = System.getProperty("target.ip", "61.128.226.218");
    public static final String TARGET_IP = System.getProperty("target.ip", "127.0.0.1");
    public static final int TARGET_PORT = Integer.valueOf(System.getProperty("target.port", "80"));
    public static final int DOCK_PORT = Integer.valueOf(System.getProperty("dock.port", "9999"));
    public static final int MSG_PORT = Integer.valueOf(System.getProperty("msg.port", "8888"));

    public static volatile byte[] serverKey = null;
    public static volatile byte[] clientKey = null;

    public static final Key RSA_KEY = RSAUtils.getPubKey(CertificateUtils.loadKey(System.getProperty("cert.filename", "rsa.pub"), System.getProperty("cert.password", "12345678")));


}
