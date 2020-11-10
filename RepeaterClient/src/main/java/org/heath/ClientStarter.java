package org.heath;

import org.heath.utils.PBEUtils;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/08
 * @Description 中继客户端启动类
 */
public class ClientStarter {
    public static void main(String[] args) {
        while (true){
            String str = "hello world";
            String passwd = "1";
            byte[] encryptedData = PBEUtils.encrypt(str.getBytes(), passwd);
            byte[] data = PBEUtils.decrypt(encryptedData, passwd);
            System.out.println(new String(data));
        }
    }
}
