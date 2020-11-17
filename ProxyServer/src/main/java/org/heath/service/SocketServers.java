package org.heath.service;

import org.heath.runner.ClientMsgHandler;
import org.heath.runner.MsgSocketServer;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public class SocketServers {
    private ClientMsgHandler clientMsgHandler;
    private MsgSocketServer msgSocketServer;

    public SocketServers(ClientMsgHandler clientMsgHandler, MsgSocketServer msgSocketServer) {
        this.clientMsgHandler = clientMsgHandler;
        this.msgSocketServer = msgSocketServer;
    }

    public void startup() {
        clientMsgHandler.boot();
        msgSocketServer.boot();
    }
}
