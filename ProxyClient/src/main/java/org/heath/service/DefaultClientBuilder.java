package org.heath.service;

import org.heath.runner.ClientMsgHandler;
import org.heath.runner.MsgSocketClient;
import org.heath.runner.ServerMsgHandler;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description 客户端启动入口构建器
 */
public class DefaultClientBuilder {

    private DefaultClientBuilder() {
    }

    public static DefaultClientBuilder create() {
        return new DefaultClientBuilder();
    }


    public ClientStarter build() {
        ArrayBlockingQueue<byte[]> clientMsgQueue = new ArrayBlockingQueue<>(20);
        ArrayBlockingQueue<byte[]> serverMsgQueue = new ArrayBlockingQueue<>(20);
        MsgSocketClient msgSocketClient = new MsgSocketClient(serverMsgQueue);
        ClientMsgHandler clientMsgHandler = new ClientMsgHandler(clientMsgQueue, msgSocketClient);
        ServerMsgHandler serverMsgHandler = new ServerMsgHandler(serverMsgQueue);
        return new ClientStarter(clientMsgHandler, serverMsgHandler, msgSocketClient);
    }
}
