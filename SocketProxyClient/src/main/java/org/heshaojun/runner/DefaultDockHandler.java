package org.heshaojun.runner;

import lombok.extern.log4j.Log4j2;
import org.heshaojun.common.CommonConst;
import org.heshaojun.common.CommonProperties;
import org.heshaojun.service.AbstractDockHandler;
import org.heshaojun.service.IChannelSelector;
import org.heshaojun.utils.MsgPackUtils;
import sun.nio.ch.DirectBuffer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author heshaojun
 * @date 2020/11/18
 * @description 实时采集对接消息对接消息，合成对接消息包，发送到消息客户端
 */
@Log4j2
public class DefaultDockHandler extends AbstractDockHandler {
    private int dockPort;
    private String serverIp;
    private int targetPort;
    private String targetIp;

    public DefaultDockHandler(IChannelSelector channelSelector) {
        super(channelSelector);
        this.dockPort = Integer.valueOf(System.getProperty("dock.port", "9999"));
        this.serverIp = System.getProperty("server.ip", "127.0.0.1");
        this.targetPort = Integer.valueOf(System.getProperty("target.port", "80"));
        this.targetIp = System.getProperty("target.ip", "61.128.226.218");
    }

    @Override
    protected void dock(byte[] data) {
        CompletableFuture.runAsync(() -> {
            asyncDock(data);
        });
    }

    private void asyncDock(byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.PACK_SIZE);
        SocketChannel target = null;
        SocketChannel dock = null;
        try {
            target = SocketChannel.open();
            dock = SocketChannel.open();
            target.connect(new InetSocketAddress(targetIp, targetPort));
            while (!target.isConnected()) {
                Thread.sleep(10);
            }
            target.configureBlocking(false);
            dock.connect(new InetSocketAddress(serverIp, dockPort));
            dock.configureBlocking(false);
            buffer.put(data);
            buffer.flip();
            while (true) {
                dock.write(buffer);
                if (buffer.position() == buffer.limit()) break;
                Thread.sleep(10);
            }
            buffer.clear();
            while (true) {
                if (dock.read(buffer) == -1) throw new Exception("读取到结尾");
                if (buffer.position() == buffer.limit()) break;
                Thread.sleep(10);
            }
            byte[] result = new byte[CommonProperties.PACK_SIZE];
            buffer.flip();
            buffer.get(result);
            Map<String, String> map = MsgPackUtils.unpack(result);
            log.info("对接结果消息为：" + map);
            if (!"OK".equals(map.get(CommonConst.DOCK))) throw new Exception("对接失败");
            log.info("对接成功");
            CommonConst.MAPPED_CHANNEL.put(target, new CommonConst.ChannelInfo(target, dock));
            CommonConst.MAPPED_CHANNEL.put(dock, new CommonConst.ChannelInfo(dock, target));
            channelSelector.registry(SelectionKey.OP_READ, dock, target);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                target.close();
            } catch (Exception e1) {
            }
            try {
                dock.close();
            } catch (Exception e1) {
            }
        } finally {
            try {
                ((DirectBuffer) buffer).cleaner().clean();
            } catch (Exception e) {
            }
        }
    }

}
