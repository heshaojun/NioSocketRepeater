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

    public static final Key RSA_KEY = RSAUtils.getPriKey(CertificateUtils.loadKey(System.getProperty("cert.filename", "rsa.pri"), System.getProperty("cert.password", "12345678")));
}
