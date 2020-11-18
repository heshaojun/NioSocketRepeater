package org.heath.test;

import org.heath.utils.CertificateUtils;
import org.heath.utils.RSAUtils;

import java.security.Key;

/**
 * @author heshaojun
 * @date 2020/11/18
 * @description TODO
 */
public class RSATest {
    public static final Key RSA_KEY_PUB = RSAUtils.getPubKey(CertificateUtils.loadKey(System.getProperty("cert.filename", "rsa.pub"), System.getProperty("cert.password", "12345678")));

}
