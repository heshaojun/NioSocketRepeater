package org.heshaojun.utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * @author heshaojun
 * @date 2020/11/10
 * @description 构建非对称加密证书
 */
public class CertificateUtils {
    //生成证书对
    public static final void generateCer(String fileName, String password) {
        Map<String, byte[]> keyPart = RSAUtils.generateKeys();
        byte[] pubKey = PBEUtils.encrypt(keyPart.get(RSAUtils.PUBLIC_KEY), password);
        byte[] priKey = PBEUtils.encrypt(keyPart.get(RSAUtils.PRIVATE_KEY), password);
        File pubKeyFile = new File(fileName + ".pub");
        File priKeyFile = new File(fileName + ".pri");
        try {
            pubKeyFile.createNewFile();
            FileOutputStream pubFileOut = new FileOutputStream(pubKeyFile);
            pubFileOut.write(pubKey);
            priKeyFile.createNewFile();
            FileOutputStream priFileOut = new FileOutputStream(priKeyFile);
            priFileOut.write(priKey);
            pubFileOut.flush();
            priFileOut.flush();
            pubFileOut.close();
            priFileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] loadKey(String fileName, String password) {
        byte[] result = null;
        try {
            File file = new File(fileName);
            InputStream inputStream = new FileInputStream(file);
            byte[] encryptKey = IOUtils.toByteArray(inputStream);
            result = PBEUtils.decrypt(encryptKey, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
