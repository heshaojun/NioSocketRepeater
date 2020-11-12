package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;
import org.heath.common.CommonStatus;
import sun.nio.ch.DirectBuffer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/08
 * @Description TODO
 */
@Log4j2
public abstract class AbstractMsgClient implements IMsgClient {
    @Override
    public void startup() {
        log.info("开始启动消息客户端");
        CommonStatus.isMsgClientAlive = true;
        SocketChannel channel = null;
        Selector selector = null;
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.PACKAGE_SIZE);
        try {
            channel = SocketChannel.open();
            selector = Selector.open();
            log.info("消息客户端连接到消息服务器：" + CommonProperties.SERVER_IP + ":" + CommonProperties.MSG_PORT);
            channel.connect(new InetSocketAddress(CommonProperties.SERVER_IP, CommonProperties.MSG_PORT));
            CommonConst.SERVER_MSG_QUEUE.clear();
            CommonConst.CLIENT_MSG_QUEUE.clear();
            log.info("开始进行安全认证和密钥交换");
            if (auth(channel)) {
                log.info("安全认证和密钥交换正常完成");
                channel.configureBlocking(false);
                log.info("将消息客户端通道到读取事件注册到选择器上");
                channel.register(selector, SelectionKey.OP_READ);
                startupClientMsgWriter(channel);
                CommonStatus.isMsgClientWorking = true;
                CommonStatus.msgClientStatus = CommonStatus.MsgClientStatus.HEALTH;
                log.info("开始轮训选择器");
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
            log.error("消息客户端运行异常", e);
        } finally {
            CommonStatus.isMsgClientAlive = false;
            CommonStatus.isMsgClientWorking = false;
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
            return true;
        }
        return false;
    }

    protected abstract boolean verify(SocketChannel channel);


    protected abstract void handleServerMsg(byte[] data);

    protected abstract void startupClientMsgWriter(SocketChannel channel);
}