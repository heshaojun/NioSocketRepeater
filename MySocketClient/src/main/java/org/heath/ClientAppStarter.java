package org.heath;

import org.heath.runner.DefaultHeartbeat;
import org.heath.runner.DefaultMsgClient;
import org.heath.runner.DefaultServerMsgHandler;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public class ClientAppStarter {
    public static void main(String[] args) {

        DefaultMsgClient msgClient = new DefaultMsgClient();
        msgClient.boot();
        new DefaultHeartbeat().boot();
        new DefaultServerMsgHandler(msgClient).boot();
        while (true) {
            try {
                Thread.sleep(1000000);
            } catch (Exception e) {
            }
        }
    }
}
