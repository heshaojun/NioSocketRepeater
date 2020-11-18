package org.heath;

import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;
import org.heath.runner.DefaultDockHandler;
import org.heath.runner.DefaultMsgChannelSelector;
import org.heath.runner.DefaultMsgServer;
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
        msgChannelSelector.boot();
        msgServer.boot();
        dockHandler.boot();
        while (true) {
            try {
                Thread.sleep(60);
                CommonConst.DOCK_MSG_QUEUE.put("" + new Date().getTime());
            } catch (Exception e) {
            }
        }
    }
}
