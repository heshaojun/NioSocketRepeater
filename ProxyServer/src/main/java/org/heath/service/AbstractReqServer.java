package org.heath.service;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description TODO
 */
public abstract class AbstractReqServer implements ISocketServer {

    @Override
    public void run() {
        startup();
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        try {
            serverSocketChannel = createServer();
            selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            workup();
            while (true) {
                if (!isAlive() || !isWorking()) break;
                if (selector.select(500) == 0) continue;
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = (SelectionKey) keys.next();

                    keys.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shutdown();
            hangup();
        }
    }

    protected abstract ServerSocketChannel createServer();

}
