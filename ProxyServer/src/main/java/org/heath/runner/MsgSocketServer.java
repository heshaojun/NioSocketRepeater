package org.heath.runner;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;
import org.heath.service.AbstractSocketServer;
import org.heath.service.IChannelSelector;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
@Log4j2
public class MsgSocketServer extends AbstractSocketServer {
    private IChannelSelector channelSelector;


    public MsgSocketServer(IChannelSelector channelSelector) {
        this.channelSelector = channelSelector;
    }

    @Override
    protected ServerSocketChannel createServer() {
        log.info("创建消息服务器，绑定端口：" + CommonProperties.MSG_PORT);
        ServerSocketChannel serverSocketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(CommonProperties.MSG_PORT), 10);
            serverSocketChannel.configureBlocking(false);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (serverSocketChannel != null) serverSocketChannel.close();
            } catch (Exception e1) {
            }
            serverSocketChannel = null;
        }
        return serverSocketChannel;
    }

    @Override
    public long getPeriod() {
        return Long.valueOf(System.getProperty("msg.server.period", "20000"));
    }


    @Override
    public void handleAccept(SelectionKey key) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel channel = serverSocketChannel.accept();
            log.info("消息服务器接收到新的连接，连接信息为：" + channel.socket().getRemoteSocketAddress());
            CompletableFuture.runAsync(() -> {
                auth(channel);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void auth(SocketChannel channel) {
        try {
            channelSelector.registry(channel, SelectionKey.OP_READ);
            CommonConst.MSG_CLIENT_INFO_MAP.put(channel, new CommonConst.MsgClientInfo(channel, "1234456667", null, new Date().getTime()));
        } catch (Exception e) {

        }
    }
}
