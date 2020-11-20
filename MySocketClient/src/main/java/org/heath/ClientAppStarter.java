package org.heath;

import org.heath.runner.*;
import org.heath.service.RepeatDataHandler;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public class ClientAppStarter {
    public static void main(String[] args) {

        DefaultMsgClient msgClient = new DefaultMsgClient();
        DefaultHeartbeat heartbeat = new DefaultHeartbeat();
        DefaultServerMsgHandler serverMsgHandler = new DefaultServerMsgHandler(msgClient);
        //对接通道处理器
        DefaultDockChannelSelector dockChannelSelector = new DefaultDockChannelSelector(new RepeatDataHandler());
        DefaultDockHandler dockHandler = new DefaultDockHandler(dockChannelSelector);

        msgClient.boot();
        heartbeat.boot();
        serverMsgHandler.boot();
        dockChannelSelector.boot();
        dockHandler.boot();

        while (true) {
            try {
                Thread.sleep(1000000);
            } catch (Exception e) {
            }
        }
    }
}
