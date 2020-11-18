package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.utils.MsgPackUtils;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
@Log4j2
public class MsgReadHandler implements IEventHandler {
    ByteBuffer buffer = ByteBuffer.allocateDirect(200);

    @Override
    public void handle(SelectionKey key) {
        if (key.isReadable()) {
            handleRead(key);
        }
    }

    private void handleRead(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        try {
            channel.read(buffer);
            if (buffer.position() == buffer.limit()) {
                byte[] data = new byte[buffer.limit()];
                buffer.flip();
                buffer.get(data);
                Map<String, String> map = MsgPackUtils.unpack(data);
                log.info("接收到的消息为：" + map);
                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                key.cancel();
            } catch (Exception e1) {
            }
        }
    }
}
