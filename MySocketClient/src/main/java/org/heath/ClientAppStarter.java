package org.heath;

import org.heath.runner.DefaultHeartbeat;
import org.heath.runner.DefaultMsgClient;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public class ClientAppStarter {
    public static void main(String[] args) {

        new DefaultMsgClient().boot();
        new DefaultHeartbeat().boot();

        while (true) {
            try {
                Thread.sleep(1000000);
            } catch (Exception e) {
            }
        }
    }
}
