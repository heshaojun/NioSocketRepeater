package org.heath.service;

import org.heath.common.CommonProperties;
import org.heath.common.CommonStatus;
import sun.nio.ch.DirectBuffer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/08
 * @Description TODO
 */
public abstract class AbstractMsgClient implements IMsgClient {
    @Override
    public void startup() {
        CommonStatus.isMsgClientAlive = true;
        SocketChannel channel = null;
        Selector selector = null;
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.PACKAGE_SIZE);
        try {
            channel = SocketChannel.open();
            selector = Selector.open();
            channel.connect(new InetSocketAddress(CommonProperties.SERVER_IP, CommonProperties.MSG_PORT));
            CommonProperties.SERVER_MSG_QUEUE.clear();
            CommonProperties.CLIENT_MSG_QUEUE.clear();
            if (auth(channel)) {
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
                startupClientMsgWriter(channel);
                while (true) {
                    if (!CommonStatus.isMsgClientAlive) break;
                    if (selector.select(100) == 0) continue;
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        if (key.isReadable()) {
                            if (channel.read(buffer) == -1)
                                CommonStatus.msgClientStatus = CommonStatus.MsgClientStatus.BREAK_CHANNEL;
                            if (buffer.position() == buffer.limit()) {
                                byte[] data = new byte[CommonProperties.PACKAGE_SIZE];
                                buffer.flip();
                                buffer.get(data);
                                CompletableFuture.runAsync(() -> {
                                    handleServerMsg(data);
                                });
                                buffer.clear();
                            } else {
                                buffer.compact();
                            }
                        }
                        keys.remove();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CommonStatus.isMsgClientAlive = false;
            try {
                if (channel != null) channel.close();
            } catch (Exception e) {
            }
            try {
                if (selector != null) selector.close();
            } catch (Exception e) {
            }
            try {
                ((DirectBuffer) buffer).cleaner().clean();
            } catch (Exception e) {
            }
        }
    }

    private boolean auth(SocketChannel channel) {
        if (verify(channel)) {
            if (exchangeKey(channel)) {
                return true;
            }
        }
        return false;
    }

    protected abstract boolean verify(SocketChannel channel);

    protected abstract boolean exchangeKey(SocketChannel channel);

    protected abstract void handleServerMsg(byte[] data);

    protected abstract void startupClientMsgWriter(SocketChannel channel);
}