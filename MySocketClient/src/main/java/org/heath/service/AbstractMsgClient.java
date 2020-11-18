package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;
import org.heath.utils.MsgPackUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
@Log4j2
public abstract class AbstractMsgClient extends AbstractAutoManager {
    private String ip;
    private int port;

    public AbstractMsgClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public AbstractMsgClient() {
        this.port = Integer.valueOf(System.getProperty("msg.port", "8888"));
        this.ip = System.getProperty("server.ip", "127.0.0.1");
    }

    @Override
    public void run() {
        log.info("启动消息客户端，开始连接到服务器：" + ip + ":" + port);
        startLife();
        Selector selector = null;
        SocketChannel channel = null;
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.PACK_SIZE);
        try {
            channel = SocketChannel.open();
            selector = Selector.open();
            channel.connect(new InetSocketAddress(ip, port));
            channel.configureBlocking(false);
            if (!auth(channel)) throw new Exception("认证失败");
            log.info("注册读取事件");
            channel.register(selector, SelectionKey.OP_READ);
            startClientMsgWriter(channel);
            startWork();
            while (true) {
                if (selector.select(500) == -1) continue;
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    if (key.isReadable()) {
                        if (channel.read(buffer) == -1) throw new IOException("读取到结尾标识");
                        if (buffer.position() == buffer.limit()) {
                            byte[] data = new byte[buffer.limit()];
                            buffer.flip();
                            buffer.get(data);
                            buffer.clear();
                            CommonConst.SERVER_MSG_QUEUE.put(data);
                        }
                    }
                    keys.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopLife();
            stopWork();
            try {
                if (channel != null) channel.close();
            } catch (Exception e) {
            }
            try {
                if (selector != null) selector.close();
            } catch (Exception e) {
            }
        }
    }

    private void startClientMsgWriter(SocketChannel channel) {
        new Thread(() -> {
            log.info("启动消息客户端，客户端自生消息写入线程");
            ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.PACK_SIZE);
            try {
                while (true) {
                    byte[] data = CommonConst.CLIENT_MSG_QUEUE.poll(2, TimeUnit.SECONDS);
                    if (data == null) continue;
                    if (!isAlive() || !isWorking()) {
                        CommonConst.CLIENT_MSG_QUEUE.clear();
                        continue;
                    }
                    buffer.clear();
                    buffer.put(data);
                    buffer.flip();
                    while (true) {
                        channel.write(buffer);
                        if (buffer.position() == buffer.limit()) break;
                        Thread.sleep(10);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stopWork();
                stopLife();
            }
        }).start();
    }

    protected abstract boolean auth(SocketChannel channel);

}
