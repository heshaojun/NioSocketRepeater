package org.heshaojun.service;

import lombok.extern.log4j.Log4j2;
import org.heshaojun.common.CommonConst;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author heshaojun
 * @date 2020/11/20
 * @description TODO
 */
@Log4j2
public class RepeatDataHandler implements IEventHandler {
    @Override
    public void handle(SelectionKey key) {
        if (key.isReadable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            try {
                CommonConst.ChannelInfo channelInfo = CommonConst.MAPPED_CHANNEL.get(channel);
                channelInfo.refresh();
                SocketChannel target = channelInfo.getMapped();
                ByteBuffer buffer = channelInfo.getBuffer();
                if (channel.read(buffer) == -1) {
                    channelInfo.destroy();
                    throw new Exception("读取到结尾");
                }
                buffer.flip();
                while (true) {
                    target.write(buffer);
                    if (buffer.position() == buffer.limit()) break;
                    Thread.sleep(10);
                }
                buffer.clear();
            } catch (Exception e) {
                try {
                    channel.close();
                } catch (Exception e1) {
                }
                try {
                    CommonConst.MAPPED_CHANNEL.get(channel).destroy();
                } catch (Exception e1) {
                }
            }
        }
    }
}
