package org.heath;

import org.heath.service.MsgClientHeartbeatStarter;
import org.heath.service.MsgClientStarter;
import org.heath.utils.AESUtils;
import org.heath.utils.PBEUtils;
import org.heath.utils.RSAUtils;

import java.util.Map;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/08
 * @Description 中继客户端启动类
 */
public class ClientStarter {
    public static void main(String[] args) {
        MsgClientStarter.startup();
        MsgClientHeartbeatStarter.startup();
        while (true) {
            try {
                Thread.sleep(100000);
            } catch (Exception e) {
            }
        }
    }
}
