package org.heath.test;

import org.heath.utils.CertificateUtils;
import org.heath.utils.RSAUtils;

import java.io.File;

/**
 * @author heshaojun
 * @date 2020/11/10
 * @description TODO
 */
public class CertificateCreateTest {
    public static void main(String[] args) {
        CertificateUtils.generateCer("rsa", "12345678");
        byte[] pubKey = CertificateUtils.loadKey("rsa.pub", "12345678");
        byte[] priKey = CertificateUtils.loadKey("rsa.pri", "12345678");
        String context = "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈";
        byte[] encryptedBytes = RSAUtils.encrypt(context.getBytes(), RSAUtils.getPubKey(pubKey));
        byte[] decryptedBytes = RSAUtils.decrypt(encryptedBytes, RSAUtils.getPriKey(priKey));
        System.out.println(new String(decryptedBytes));
        System.out.println((new File("").getAbsolutePath()));
    }
}
