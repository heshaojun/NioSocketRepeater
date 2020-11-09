package org.heath.service;

import org.heath.common.CommonProperties;
import org.heath.common.CommonStatus;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
public class MsgClient extends AbstractMsgClient {


    @Override
    protected boolean verify(SocketChannel channel) {
        return true;
    }

    @Override
    protected boolean exchangeKey(SocketChannel channel) {

        return true;
    }

    @Override
    public void handleServerMsg(byte[] data) {
        if (!CommonStatus.isMsgClientAlive) return;
        try {
            CommonProperties.SERVER_MSG_QUEUE.put(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startupClientMsgWriter(SocketChannel channel) {
        new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.PACKAGE_SIZE);
            try {
                while (true) {
                    if (!CommonStatus.isMsgClientAlive) break;
                    byte[] data = CommonProperties.CLIENT_MSG_QUEUE.take();
                    buffer.clear();
                    buffer.put(data);
                    buffer.flip();
                    while (true) {
                        channel.write(buffer);
                        if (buffer.position() == 0) break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                CommonStatus.isMsgClientAlive = false;
                try {
                    channel.close();
                } catch (Exception e) {
                }
                try {
                    ((DirectBuffer) buffer).cleaner().clean();
                } catch (Exception e) {
                }
            }
        }).start();
    }
}
