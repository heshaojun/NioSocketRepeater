package org.heath.service;

import lombok.extern.log4j.Log4j2;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * @author heshaojun
 * @date 2020/11/19
 * @description
 */
@Log4j2
public abstract class AbstractProxyServer extends AbstractAutoManager {
    private int port;
    private int logback;

    public AbstractProxyServer() {
        this.port = Integer.valueOf(System.getProperty("proxy.server.port", "8080"));
        this.logback = Integer.valueOf(System.getProperty("proxy.server.logback", "100"));
    }

    @Override
    public void run() {
        log.info("启动代理服务器端口");
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

    protected abstract void handleAccept(SelectionKey key);
}
