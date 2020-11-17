package org.heath.service;

import org.heath.runner.ClientMsgHandler;
import org.heath.runner.MsgSocketServer;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public class SocketServersBuilder {
    private ClientMsgHandler clientMsgHandler;
    private MsgSocketServer msgSocketServer;
    private AbstractChannelSelector channelSelector;
    private IClientMsgProcessor clientMsgProcessor;

    private SocketServersBuilder() {
    }

    public static SocketServersBuilder create() {
        return new SocketServersBuilder();
    }

    public SocketServers build() {
        if (clientMsgProcessor == null) clientMsgProcessor = new MsgProcessor();
        if (channelSelector == null) channelSelector = new MsgChannelSelector();
        if (msgSocketServer == null) msgSocketServer = new MsgSocketServer(channelSelector);
        if (clientMsgHandler == null) clientMsgHandler = new ClientMsgHandler(channelSelector, clientMsgProcessor);
        return new SocketServers(clientMsgHandler, msgSocketServer);
    }
}
