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
public abstract class AbstractMsgSocketClient extends AbstractLifeManager implements ISocketClient, IMsgListener {

    private volatile SocketChannel channel;


    @Override
    public void run() {
        log.info("开始启动消息客户端，连接服务器信息：" + CommonProperties.SERVER_IP + "：" + CommonProperties.MSG_PORT);
        this.channel = connect(CommonProperties.SERVER_IP, CommonProperties.MSG_PORT);
        if (channel == null) return;
        startup();
        Selector selector = null;
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.MSG_PACK_SIZE);
        workup();
        try {
            selector = Selector.open();
            if (!auth(channel)) throw new Exception("认证失败");
            log.info("消息客户端通过消息服务器到认证");
            channel.register(selector, SelectionKey.OP_READ);
            log.info("消息客户端开始工作");
            while (true) {
                if (!isAlive() || !isWorking()) break;
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
            shutdown();
            hangup();
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


    protected abstract void handleMsg(byte[] data);

    protected abstract boolean auth(SocketChannel channel);

    public SocketChannel getChannel() {
        return channel;
    }

    @Override
    public void update() {
        hangup();
    }
}
