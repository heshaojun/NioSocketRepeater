package org.heath.service;

import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/08
 * @Description TODO
 */
public abstract class AbstractMsgClient extends AbstractClient implements ILifeMonitor, Runnable {
    private IClientChannelHandler msClientChannelHandler;
    private ArrayBlockingQueue<byte[]> ackMsgQueue;
    private int dataPackageSize = 300;
    private volatile boolean isAlive = false;
    private volatile boolean isWorking = false;


    public AbstractMsgClient(String ip, int port, int dataPackageSize, IClientChannelHandler msClientChannelHandler, ArrayBlockingQueue<byte[]> ackMsgQueue) {
        super(ip, port);
        this.dataPackageSize = dataPackageSize;
        this.msClientChannelHandler = msClientChannelHandler;
        this.ackMsgQueue = ackMsgQueue;
    }


    public void run() {
        monitor();
    }

    public void monitor() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isAlive) {
                    if (!isWorking) {
                        workup();
                    }
                }
            }
        }, 100, 1000);
    }

    public void workup() {
        isAlive = true;
        SocketChannel channel = connect();
        if (channel == null) return;
        if (channel.isConnected()) return;
        Selector selector = null;
        try {
            selector = Selector.open();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
            isWorking = true;
            ackMsgQueue.clear();
            startupMsgWriter(channel);
            while (true) {
                if (!isAlive) break;
                if (selector.select(1000) == 0) continue;
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    if (key.isReadable()) {
                        msClientChannelHandler.handleRead(key);
                    }
                    keys.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (channel != null) channel.close();
            } catch (Exception e) {
            }
            try {
                if (selector != null) selector.close();
            } catch (Exception e) {
            }
            isAlive = false;
        }
    }

    public void startupMsgWriter(SocketChannel channel) {
        new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocateDirect(dataPackageSize);
            try {
                while (true) {
                    byte[] writDate = ackMsgQueue.take();
                    if (writDate.length == dataPackageSize) {
                        buffer.clear();
                        buffer.put(writDate);
                        channel.write(buffer);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    channel.close();
                } catch (Exception e) {
                }
                try {
                    ((DirectBuffer) buffer).cleaner().clean();
                } catch (Exception e) {
                }
            }
        }).start();
    }
}
