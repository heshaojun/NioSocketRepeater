package org.heshaojun.service;

import org.heshaojun.common.CommonConst;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;

/**
 * @author heshaojun
 * @date 2020/11/20
 * @description TODO
 */
public class RepeatDataHandler implements IEventHandler {
    @Override
    public void handle(SelectionKey key) {
        if (key.isReadable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            try {
                CommonConst.ChannelInfo channelInfo = CommonConst.MAPPED_CHANNEL.get(channel);
                if (!channelInfo.lock()) return;
                CompletableFuture.runAsync(() -> {
                    read(channelInfo, channel);
                });
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

    private void read(CommonConst.ChannelInfo channelInfo, SocketChannel channel) {
        try {
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
            channelInfo.unlock();
        } catch (Exception e) {
           // e.printStackTrace();
            try {
                channel.close();
            } catch (Exception e1) {
            }
            try {
                CommonConst.MAPPED_CHANNEL.get(channel).destroy();
            } catch (Exception e1) {
            }
        } finally {

        }
    }
}
