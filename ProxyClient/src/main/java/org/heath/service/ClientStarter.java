package org.heath.service;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description 客户端启动入口
 */
public class ClientStarter {
    private Runnable clientMsgHandler;
    private Runnable serverMsgHandler;
    private Runnable msgSocketClient;

    public ClientStarter(Runnable clientMsgHandler, Runnable serverMsgHandler, Runnable msgSocketClient) {
        this.clientMsgHandler = clientMsgHandler;
        this.serverMsgHandler = serverMsgHandler;
        this.msgSocketClient = msgSocketClient;
    }

    public void startup() {
        new Thread(clientMsgHandler).start();
        new Thread(serverMsgHandler).start();
        new Thread(msgSocketClient).start();
    }
}
