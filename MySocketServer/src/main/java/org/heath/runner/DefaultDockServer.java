package org.heath.runner;

import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;
import org.heath.service.AbstractDockServer;
import org.heath.service.IChannelSelector;
import org.heath.utils.MsgPackUtils;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author heshaojun
 * @date 2020/11/20
 * @description 默认对接服务器
 */
public class DefaultDockServer extends AbstractDockServer {
    public DefaultDockServer(IChannelSelector channelRegister) {
        super(channelRegister);
    }

    @Override
    protected void dock(SocketChannel channel) {
        CompletableFuture.runAsync(() -> {
            asyncDock(channel);
        });
    }

    //异步处理，防止阻塞
    private void asyncDock(SocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.PACK_SIZE);
        try {
            //读取10秒，停止读取
            for (int i = 0; i < 1000; i++) {
                if (channel.read(buffer) == -1) throw new Exception("读取到结尾");
                if (buffer.limit() == buffer.position()) break;
                Thread.sleep(10);
            }
            if (buffer.limit() != buffer.position()) throw new Exception("读取数据不完整");
            //解析数据
            byte[] data = new byte[buffer.limit()];
            buffer.flip();
            buffer.get(data);
            Map<String, String> map = MsgPackUtils.unpack(data);
            if (map == null) throw new Exception("数据包格式异常");
            String dockId = map.get(CommonConst.DOCK);
            if (dockId == null || "".equals(dockId)) throw new Exception("数据包格式异常");
            SocketChannel cachedChannel = CommonConst.CACHED_CHANNEL_INFO_MAP.remove(dockId).getChannel();
            if (cachedChannel == null) throw new Exception("没有对应的通道");
            if (cachedChannel.socket().isClosed() || cachedChannel.socket().isOutputShutdown() || cachedChannel.socket().isInputShutdown())
                throw new Exception("对应的通道不可用");
            //发送验证对接成功数据
            map.put(CommonConst.DOCK, "OK");
            buffer.clear();
            data = MsgPackUtils.pack(map, "DOCK_RESULT", CommonProperties.PACK_SIZE);
            buffer.put(data);
            buffer.flip();
            while (true) {
                channel.write(buffer);
                if (buffer.position() == buffer.limit()) break;
                Thread.sleep(10);
            }
            CommonConst.MAPPED_CHANNEL.put(cachedChannel, new CommonConst.ChannelInfo(cachedChannel, channel));
            CommonConst.MAPPED_CHANNEL.put(channel, new CommonConst.ChannelInfo(channel, cachedChannel));
            channelRegister.registry(SelectionKey.OP_READ, channel, cachedChannel);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                channel.close();
            } catch (Exception e1) {
            }
        } finally {
            try {
                ((DirectBuffer) buffer).cleaner().clean();
            } catch (Exception e1) {
            }
        }
    }
}
