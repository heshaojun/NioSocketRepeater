package org.heath;

import org.heath.service.MsgServer;
import org.heath.service.MsgServerStarter;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/08
 * @Description 中继服务器启动类
 */
public class ServerStarter {
    public static void main(String[] args) {
        MsgServerStarter.startup();
        while (true) {
            try {
                Thread.sleep(100000);
            } catch (Exception e) {
            }
        }
    }
}
