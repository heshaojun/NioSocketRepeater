package org.heath.service;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description 客户端启动入口
 */
public class ClientStarter {
    private ILifeManager clientMsgHandler;
    private ILifeManager serverMsgHandler;
    private ILifeManager msgSocketClient;

    public ClientStarter(ILifeManager clientMsgHandler, ILifeManager serverMsgHandler, ILifeManager msgSocketClient) {
        this.clientMsgHandler = clientMsgHandler;
        this.serverMsgHandler = serverMsgHandler;
        this.msgSocketClient = msgSocketClient;
    }

    public void startup() {
        clientMsgHandler.boot();
        serverMsgHandler.boot();
        msgSocketClient.boot();
    }
}
