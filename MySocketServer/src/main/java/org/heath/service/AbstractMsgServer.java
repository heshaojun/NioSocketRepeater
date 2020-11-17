package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.utils.MsgPackUtils;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
@Log4j2
public abstract class AbstractMsgServer implements Runnable {
    private IChannelRegister channelRegister;

    public AbstractMsgServer(IChannelRegister channelRegister) {
        this.channelRegister = channelRegister;
    }

    @Override
    public void run() {
        log.info("启动消息服务器");
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            serverSocketChannel.bind(new InetSocketAddress(8888), 10);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                if (selector.select(500) == 0) continue;
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    }
                    keys.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAccept(SelectionKey key) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel channel = serverSocketChannel.accept();
            log.info("消息客户端接收到来自：" + channel.getRemoteAddress() + "的连接");
            channel.configureBlocking(false);
            channelRegister.registry(channel, SelectionKey.OP_READ);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
