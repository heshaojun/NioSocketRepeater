package org.heath;

import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;
import org.heath.runner.DefaultDockHandler;
import org.heath.runner.DefaultMsgChannelSelector;
import org.heath.runner.DefaultMsgServer;
import org.heath.runner.DefaultProxyServer;
import org.heath.service.MsgReadHandler;

import java.util.Date;

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
        DefaultDockHandler dockHandler = new DefaultDockHandler();
        DefaultProxyServer proxyServer = new DefaultProxyServer();
        msgChannelSelector.boot();
        msgServer.boot();
        dockHandler.boot();
        proxyServer.boot();
        while (true) {
            try {
                Thread.sleep(60);
            } catch (Exception e) {
            }
        }
    }
}
