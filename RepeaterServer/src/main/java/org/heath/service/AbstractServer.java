package org.heath.service;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * @author heshaojun
 * @date 2020/11/12
 * @description TODO
 */
@Log4j2
public abstract class AbstractServer implements IServer {

    @Override
    public void startup() {
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        try {
            serverSocketChannel = createServer();
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            workup(serverSocketChannel, selector);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("socket服务运行异常", e);
        } finally {
            try {
                serverSocketChannel.close();
            } catch (Exception e) {
            }
            try {
                selector.close();
            } catch (Exception e) {
            }
        }
    }

    protected abstract ServerSocketChannel createServer() throws IOException;


    protected void workup(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        while (true) {
            if (selector.select(100) == -1) continue;
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                if (key.isAcceptable()) {
                    handleAccept(key);
                } else if (key.isReadable()) {
                    handleRead(key);
                }
                keys.remove();
            }
        }
    }

    protected abstract void handleAccept(SelectionKey key);

    protected abstract void handleRead(SelectionKey key);
}
