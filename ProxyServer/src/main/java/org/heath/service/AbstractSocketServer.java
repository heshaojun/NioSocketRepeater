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
public abstract class AbstractSocketServer extends AbstractLifeManager implements ISocketServer {

    @Override
    public void run() {
        startup();
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        try {
            serverSocketChannel = createServer();
            if (serverSocketChannel == null) throw new Exception("创建socket服务器失败");
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
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    }
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
