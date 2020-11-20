package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonConst;
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

    @Override
    public void handle(SelectionKey key) {
        if (key.isReadable()) {
            handleRead(key);
        }
    }

    private void handleRead(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        try {
            CommonConst.MsgClientInfo msgClientInfo = CommonConst.MSG_CLIENT_INFO_MAP.get(channel);
            ByteBuffer buffer = msgClientInfo.getBuffer();
            channel.read(buffer);
            if (buffer.position() == buffer.limit()) {
                byte[] data = new byte[buffer.limit()];
                buffer.flip();
                buffer.get(data);
                byte[] clientKey = CommonConst.MSG_CLIENT_INFO_MAP.get(channel).getClientKey();
                Map<String, String> map = MsgPackUtils.secretUnpack(data, clientKey);
                log.info("接收到的消息为：" + map);
                switch (map.get(CommonConst.TYPE)) {
                    case CommonConst.HEARTBEAT_TYPE:
                        handleHeartbeat(channel);
                        break;
                    default:
                        break;
                }
                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                key.cancel();
            } catch (Exception e1) {
            }
            try {
                CommonConst.MSG_CLIENT_INFO_MAP.remove(channel);
            } catch (Exception e1) {
            }
            try {
                channel.close();
            } catch (Exception e1) {
            }
        }
    }

    private void handleHeartbeat(SocketChannel channel) {
        try {
            CommonConst.MSG_CLIENT_INFO_MAP.get(channel).refresh();
        } catch (Exception e) {
        }
    }
}
