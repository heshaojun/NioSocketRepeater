package org.heshaojun;

import org.heshaojun.runner.*;
import org.heshaojun.service.MsgReadHandler;
import org.heshaojun.service.RepeatDataHandler;

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
        //健康保持
        HealthKeeper healthKeeper = new HealthKeeper();

        msgChannelSelector.boot();
        msgServer.boot();
        dockHandler.boot();
        proxyServer.boot();
        dockChannelSelector.boot();
        dockServer.boot();
        healthKeeper.boot();
        while (true) {
            try {
                Thread.sleep(60);
            } catch (Exception e) {
            }
        }
    }
}
