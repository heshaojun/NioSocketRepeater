package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonProperties;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/15
 * @Description 消息客户端
 */
@Log4j2
public abstract class AbstractMsgClient implements IMsgClient {


    private IClientAuthHandler authHandler;
    private IClientMsgHandler clientMsgHandler;

    private volatile boolean work = false;
    private volatile boolean alive = false;
    private volatile SocketChannel channel;

    public AbstractMsgClient(IClientAuthHandler authHandler, IClientMsgHandler clientMsgHandler) {
        this.authHandler = authHandler;
        this.clientMsgHandler = clientMsgHandler;
    }

    @Override
    public boolean isWorking() {
        return work;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void startupWork() {
        work = true;
    }

    @Override
    public void startupLife() {
        alive = true;
    }

    @Override
    public void stopWork() {
        work = false;
    }

    @Override
    public void kill() {
        try {
            if (channel != null) channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        alive = false;
    }

    @Override
    public void startup() {
        log.info("开始启动消息客户端，连接服务器信息：" + CommonProperties.SERVER_IP + "：" + CommonProperties.MSG_PORT);
        this.channel = connect(CommonProperties.SERVER_IP, CommonProperties.MSG_PORT);
        if (channel == null) return;
        startupLife();
        Selector selector = null;
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.MSG_PACK_SIZE);
        try {
            selector = Selector.open();
            if (!authHandler.auth(channel)) throw new Exception("认证失败");
            log.info("消息客户端通过消息服务器到认证");
            channel.register(selector, SelectionKey.OP_READ);
            startupWriter();
            log.info("消息客户端开始工作");
            while (true) {
                if (selector.select(500) == 0) continue;
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    if (key.isReadable()) {
                        if (channel.read(buffer) == -1) throw new Exception("读取到结尾标志");
                        if (buffer.position() == buffer.limit()) {
                            log.debug("接收到一个完整到数据包");
                            byte[] data = new byte[CommonProperties.MSG_PACK_SIZE];
                            buffer.flip();
                            buffer.get(data);
                            buffer.clear();
                            handleMsg(data);
                        }
                    }
                    keys.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kill();
            stopWork();
            try {
                channel.close();
            } catch (Exception e) {
            }
            try {
                selector.close();
            } catch (Exception e) {
            }
            try {
                ((DirectBuffer) buffer).cleaner().clean();
            } catch (Exception e) {
            }
        }
        log.info("消息客户端停止工作");
    }

    protected void startupWriter() {
        log.info("启动消息客户端，消息写入线程");
        new Thread(() -> {
            while (true) {
                if (!isAlive()) break;
                clientMsgHandler.handle(channel);
            }
        }).start();
        log.info("消息写入线程停止");
    }

    protected abstract void handleMsg(byte[] data);

}
