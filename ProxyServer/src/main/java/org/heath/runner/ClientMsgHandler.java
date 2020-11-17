package org.heath.runner;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonConst;
import org.heath.service.AbstractChannelSelector;
import org.heath.service.IClientMsgProcessor;
import sun.nio.ch.DirectBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
@Log4j2
public class ClientMsgHandler {
    private AbstractChannelSelector channelSelector;
    private IClientMsgProcessor clientMsgProcessor;

    public ClientMsgHandler(AbstractChannelSelector channelSelector, IClientMsgProcessor clientMsgProcessor) {
        this.channelSelector = channelSelector;
        this.clientMsgProcessor = clientMsgProcessor;
    }

    public void boot() {
        log.info("消息处理器线程启动");
        for (Selector selector : channelSelector.getSelectors()) {
            new Thread(() -> {
                log.info("启动消息处理线程：" + Thread.currentThread().getName());
                while (true) {
                    try {
                        if (selector.select(500) == 0) continue;
                        log.info("有就绪事件");
                        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                        while (keys.hasNext()) {
                            SelectionKey key = keys.next();
                            if (key.isReadable()) {
                                SocketChannel channel = (SocketChannel) key.channel();
                                try {
                                    CommonConst.MsgClientInfo msgClientInfo = CommonConst.MSG_CLIENT_INFO_MAP.get(channel);
                                    try {
                                        if (msgClientInfo == null) throw new Exception("没有映射的数据");
                                        ByteBuffer buffer = msgClientInfo.getBuffer();
                                        if (channel.read(buffer) == -1) throw new IOException("读取到结尾");
                                        if (buffer.position() == buffer.limit()) {
                                            byte[] data = new byte[buffer.limit()];
                                            buffer.flip();
                                            buffer.get(data);
                                            buffer.clear();
                                            channel.register(key.selector(), SelectionKey.OP_READ);
                                            clientMsgProcessor.handleMsg(data);
                                        }
                                    } catch (Exception e) {
                                        try {
                                            ((DirectBuffer) msgClientInfo.getBuffer()).cleaner().clean();
                                        } catch (Exception e1) {
                                        }
                                        try {
                                            channel.close();
                                        } catch (Exception e1) {
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    try {
                                        CommonConst.MSG_CLIENT_INFO_MAP.remove(channel);
                                    } catch (Exception e1) {
                                    }
                                }
                            }
                            keys.remove();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
