package org.heath;

import org.heath.runner.DefaultMsgChannelSelector;
import org.heath.runner.DefaultMsgServer;
import org.heath.service.MsgReadHandler;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public class ServerAppStarter {
    public static void main(String[] args) {

        MsgReadHandler readHandler = new MsgReadHandler();
        DefaultMsgChannelSelector msgChannelSelector = new DefaultMsgChannelSelector(readHandler);
        DefaultMsgServer msgServer = new DefaultMsgServer(msgChannelSelector);

        msgChannelSelector.boot();
        msgServer.boot();
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
            }
        }
    }
}
