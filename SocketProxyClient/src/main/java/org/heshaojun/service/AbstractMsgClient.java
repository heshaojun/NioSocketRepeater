package org.heshaojun.service;

import lombok.extern.log4j.Log4j2;
import org.heshaojun.common.CommonConst;
import org.heshaojun.common.CommonProperties;

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
    private SocketChannel channel = null;

    public AbstractMsgClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public AbstractMsgClient() {
        this.port = CommonProperties.MSG_PORT;
        this.ip = CommonProperties.SERVER_IP;
    }

    @Override
    public void run() {
        log.info("启动消息客户端，开始连接到服务器：" + ip + ":" + port);
        startLife();
        Selector selector = null;
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.PACK_SIZE);
        try {
            channel = SocketChannel.open();
            selector = Selector.open();
            channel.connect(new InetSocketAddress(ip, port));
            channel.configureBlocking(false);
            if (!auth(channel)) throw new Exception("认证失败");
            log.info("完成认证，注册读取事件");
            channel.register(selector, SelectionKey.OP_READ);
            startClientMsgWriter();
            startWork();
            while (true) {
                if (!isWorking() || !isAlive()) break;
                if (selector.select(100) == 0) continue;
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
            log.info("消息客户端停止工作");
            try {
                if (channel != null) channel.close();
            } catch (Exception e) {
            }
            try {
                if (selector != null) selector.close();
            } catch (Exception e) {
            }
            stopLife();
            stopWork();
        }
    }

    private void startClientMsgWriter() {
        new Thread(() -> {
            log.info("启动消息客户端，客户端自生消息写入线程");
            ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.PACK_SIZE);
            try {
                CommonConst.CLIENT_MSG_QUEUE.clear();
                while (true) {
                    byte[] data = CommonConst.CLIENT_MSG_QUEUE.poll(100, TimeUnit.MILLISECONDS);
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
                        if (!isAlive() || !isWorking()) {
                            CommonConst.CLIENT_MSG_QUEUE.clear();
                            continue;
                        }
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
