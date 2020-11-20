package org.heath;

import org.heath.runner.*;
import org.heath.service.MsgReadHandler;
import org.heath.service.RepeatDataHandler;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public class ServerAppStarter {
    public static void main(String[] args) {

        //消息服务器通道注册
        DefaultMsgChannelSelector msgChannelSelector = new DefaultMsgChannelSelector(new MsgReadHandler());
        //消息服务器
        DefaultMsgServer msgServer = new DefaultMsgServer(msgChannelSelector);
        //对接消息处理器
        DefaultDockHandler dockHandler = new DefaultDockHandler();
        //代理服务器
        DefaultProxyServer proxyServer = new DefaultProxyServer();
        //对接通道处理器
        DefaultDockChannelSelector dockChannelSelector = new DefaultDockChannelSelector(new RepeatDataHandler());
        //对接服务器
        DefaultDockServer dockServer = new DefaultDockServer(dockChannelSelector);

        msgChannelSelector.boot();
        msgServer.boot();
        dockHandler.boot();
        proxyServer.boot();
        dockChannelSelector.boot();
        dockServer.boot();
        while (true) {
            try {
                Thread.sleep(60);
            } catch (Exception e) {
            }
        }
    }
}
