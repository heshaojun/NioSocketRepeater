package org.heshaojun.service;

import lombok.extern.log4j.Log4j2;
import org.heshaojun.common.CommonProperties;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
@Log4j2
public abstract class AbstractMsgServer extends AbstractAutoManager {
    private IChannelSelector channelRegister;
    private int port;
    private int logback;

    public AbstractMsgServer(IChannelSelector channelRegister) {
        this.channelRegister = channelRegister;
        this.port = CommonProperties.MSG_SERVER_PORT;
        this.logback = CommonProperties.MSG_SERVER_LOGBACK;
    }

    @Override
    public void run() {
        log.info("启动消息服务器");
        startLife();
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            serverSocketChannel.bind(new InetSocketAddress(port), logback);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            startWork();
            while (true) {
                if (selector.select(100) == 0) {
                    Thread.sleep(20);
                    continue;
                }
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
        } finally {
            try {
                if (selector != null) selector.close();
            } catch (Exception e) {
            }
            try {
                if (serverSocketChannel != null) serverSocketChannel.close();
            } catch (Exception e) {
            }
            startWork();
            startLife();
        }
    }

    private void handleAccept(SelectionKey key) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel channel = serverSocketChannel.accept();
            log.info("消息客户端接收到来自：" + channel.getRemoteAddress() + "的连接");
            channel.configureBlocking(false);
            if (auth(channel)) {
                log.info("客户端认证成功，开始注册读取事件");
                channelRegister.registry(SelectionKey.OP_READ, channel);
            } else {
                channel.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract boolean auth(SocketChannel channel);


}
