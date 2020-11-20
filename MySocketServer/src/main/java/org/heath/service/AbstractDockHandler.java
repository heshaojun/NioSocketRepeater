package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;
import org.heath.utils.MsgPackUtils;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author heshaojun
 * @date 2020/11/18
 * @description 获取对接消息队列中的对接信息，生成对接数据包，发送到消息客户端
 */
@Log4j2
public abstract class AbstractDockHandler implements IRunner {
    @Override
    public void boot() {
        new Thread(() -> {
            log.info("启动对接信息处理器线程");
            handler();
        }).start();
    }

    public void handler() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.PACK_SIZE);
        Map<String, String> map = new Hashtable<>();
        map.put(CommonConst.TYPE, CommonConst.DOCK_TYPE);
        while (true) {
            try {

                String dockerId = CommonConst.DOCK_MSG_QUEUE.take();
                map.put(CommonConst.DOCK, dockerId);
                while (true) {
                    if (CommonConst.MSG_CLIENT_INFO_MAP.isEmpty()) {
                        Thread.sleep(100);
                        continue;
                    }
                    int index = dockerId.hashCode() % CommonConst.MSG_CLIENT_INFO_MAP.size();
                    CommonConst.MSG_CLIENT_INFO_MAP.entrySet();
                    Map.Entry<SocketChannel, CommonConst.MsgClientInfo> entry = (Map.Entry<SocketChannel, CommonConst.MsgClientInfo>) CommonConst.MSG_CLIENT_INFO_MAP.entrySet().toArray()[index];
                    CommonConst.MsgClientInfo clientInfo = entry.getValue();
                    SocketChannel channel = clientInfo.getChannel();
                    if (channel.socket().isClosed() || channel.socket().isInputShutdown() || channel.socket().isOutputShutdown()) {
                        try {
                            channel.close();
                        } catch (Exception e) {
                        }
                        CommonConst.MSG_CLIENT_INFO_MAP.remove(channel);
                        continue;
                    }
                    byte[] data = MsgPackUtils.secretPack(map, clientInfo.getId(), CommonProperties.PACK_SIZE, clientInfo.getServerKey());
                    log.info("向通道：" + channel.getRemoteAddress() + " 写入对接数据:" + new String(data));
                    buffer.clear();
                    buffer.put(data);
                    buffer.flip();
                    while (true) {
                        channel.write(buffer);
                        if (buffer.position() == buffer.limit()) break;
                        Thread.sleep(10);
                    }
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
