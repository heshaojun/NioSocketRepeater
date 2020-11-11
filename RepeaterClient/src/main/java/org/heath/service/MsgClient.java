package org.heath.service;

import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;
import org.heath.common.CommonStatus;
import org.heath.utils.AESUtils;
import org.heath.utils.Base64Utils;
import org.heath.utils.MsgPackageUtils;
import sun.nio.ch.DirectBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;
import java.util.UUID;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
public class MsgClient extends AbstractMsgClient {


    @Override
    protected boolean verify(SocketChannel channel) {
        boolean result = false;
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.AUTH_PACKAGE_SIZE);
        try {
            //读取token
            for (int i = 0; i < 1000; i++) {
                if (channel.read(buffer) == -1) throw new IOException("read end");
                if (buffer.position() == buffer.limit()) break;
                Thread.sleep(10);
            }
            buffer.flip();
            byte[] data = new byte[CommonProperties.AUTH_PACKAGE_SIZE];
            buffer.get(data);
            Hashtable<String, String> dataMap = MsgPackageUtils.unpackAuthDate(data);
            if (dataMap == null) return false;
            String token = dataMap.get(CommonConst.TOKEN);
            String serverId = dataMap.get(CommonConst.SERVER_ID);
            String serverKey = dataMap.get(CommonConst.KEY);
            if (token == null || "".equals(token) || serverId == null || "".equals(serverId) || serverKey == null || "".equals(serverKey))
                return false;
            CommonProperties.msgServerId = serverId;
            CommonProperties.serverAESKey = Base64Utils.decode(serverKey);
            //写入token
            String clientId = UUID.randomUUID().toString().replace("-", "");
            CommonProperties.msgClientId = clientId;
            dataMap.remove(CommonConst.SERVER_ID);
            byte[] clientAESKey = AESUtils.generateKey();
            CommonProperties.clientAESKey = clientAESKey;
            String clientKey = Base64Utils.encodeToString(clientAESKey);
            dataMap.put(CommonConst.KEY, clientKey);
            data = MsgPackageUtils.packAuthData(dataMap);
            if (data == null) return false;
            buffer.clear();
            buffer.put(data);
            buffer.flip();
            for (int i = 0; i < 1000; i++) {
                channel.write(buffer);
                if (buffer.position() == buffer.limit()) break;
                Thread.sleep(10);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                channel.close();
            } catch (Exception e1) {
            }
            result = false;
        } finally {
            try {
                ((DirectBuffer) buffer).cleaner().clean();
            } catch (Exception e) {
            }
        }
        return result;
    }

    @Override
    public void handleServerMsg(byte[] data) {
        if (!CommonStatus.isMsgClientAlive) return;
        try {
            CommonConst.SERVER_MSG_QUEUE.put(data);
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
                    byte[] data = CommonConst.CLIENT_MSG_QUEUE.take();
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
