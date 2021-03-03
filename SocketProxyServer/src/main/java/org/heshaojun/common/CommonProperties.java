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

    public static final int DOCK_REGISTER_SIZE = Integer.valueOf(System.getProperty("dock.register.size", "4"));
    public static final int MSG_REGISTER_SIZE = Integer.valueOf(System.getProperty("msg.register.size", "1"));
    public static final int DOCK_SERVER_PORT = Integer.valueOf(System.getProperty("dock.server.port", "9999"));
    public static final int DOCK_SERVER_LOGBACK = Integer.valueOf(System.getProperty("dock.server.logback", "50"));

    public static final int MSG_SERVER_PORT = Integer.valueOf(System.getProperty("msg.server.port", "8888"));
    public static final int MSG_SERVER_LOGBACK = Integer.valueOf(System.getProperty("msg.server.logback", "10"));

    public static final int PROXY_SERVER_PORT = Integer.valueOf(System.getProperty("proxy.server.port", "80"));
    public static final int PROXY_SERVER_LOGBACK = Integer.valueOf(System.getProperty("proxy.server.logback", "100"));

    public static final Key RSA_KEY = RSAUtils.getPriKey(CertificateUtils.loadKey(System.getProperty("cert.filename", "rsa.pri"), System.getProperty("cert.password", "8cc8ec27")));
}
