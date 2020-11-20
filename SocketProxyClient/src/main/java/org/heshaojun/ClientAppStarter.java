package org.heshaojun;

import org.heshaojun.runner.*;
import org.heshaojun.service.RepeatDataHandler;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public class ClientAppStarter {
    public static void main(String[] args) {

        //默认消息客户端
        DefaultMsgClient msgClient = new DefaultMsgClient();
        //默认心跳处理
        DefaultHeartbeat heartbeat = new DefaultHeartbeat();
        //默认服务器消息处理器
        DefaultServerMsgHandler serverMsgHandler = new DefaultServerMsgHandler(msgClient);
        //对接通道处理器
        DefaultDockChannelSelector dockChannelSelector = new DefaultDockChannelSelector(new RepeatDataHandler());
        //默认对接处理器
        DefaultDockHandler dockHandler = new DefaultDockHandler(dockChannelSelector);
        //健康保持器
        HealthKeeper healthKeeper = new HealthKeeper();

        msgClient.boot();
        heartbeat.boot();
        serverMsgHandler.boot();
        dockChannelSelector.boot();
        dockHandler.boot();
        healthKeeper.boot();

        while (true) {
            try {
                Thread.sleep(1000000);
            } catch (Exception e) {
            }
        }
    }
}
