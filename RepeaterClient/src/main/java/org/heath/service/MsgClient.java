package org.heath.service;

import lombok.extern.log4j.Log4j2;
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
import java.util.concurrent.TimeUnit;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
@Log4j2
public class MsgClient extends AbstractMsgClient {


    @Override
    protected boolean verify(SocketChannel channel) {
        boolean result = false;
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.AUTH_PACKAGE_SIZE);
        try {
            log.info("读取消息服务器到认证数据");
            //读取token
            for (int i = 0; i < 10000; i++) {
                if (channel.read(buffer) == -1) throw new IOException("read end");
                if (buffer.position() == buffer.limit()) break;
                Thread.sleep(10);
            }
            if (buffer.position() != buffer.limit()) throw new IOException("读取到到数据不完整");
            buffer.flip();
            byte[] data = new byte[CommonProperties.AUTH_PACKAGE_SIZE];
            buffer.get(data);
            Hashtable<String, String> dataMap = MsgPackageUtils.unpackAuthDate(data);
            if (dataMap == null) return false;
            String token = dataMap.get(CommonConst.TOKEN);
            String serverId = dataMap.get(CommonConst.SERVER_ID);
            String serverKey = dataMap.get(CommonConst.KEY);
            log.debug("解析读取到消息服务器到数据 token:" + token + "  serverId:" + serverId + "  serverKey:" + serverKey);
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
            log.debug("生成消息客户端到认证消息： clientId:" + clientId + "  clientKey:" + clientKey);
            if (data == null) return false;
            buffer.clear();
            buffer.put(data);
            buffer.flip();
            log.info("向消息服务器写入认证数据");
            for (int i = 0; i < 10000; i++) {
                channel.write(buffer);
                if (buffer.position() == buffer.limit()) break;
                Thread.sleep(10);
            }
            if (buffer.position() != buffer.limit()) throw new IOException("写入到数据不完整");
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
        log.info("启动消息客户端消息写入线程");
        new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.PACKAGE_SIZE);
            try {
                while (true) {
                    if (!CommonStatus.isMsgClientAlive) break;
                    byte[] data = CommonConst.CLIENT_MSG_QUEUE.poll(100, TimeUnit.MILLISECONDS);
                    if (data == null) continue;
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
                log.error("写入消息异常", e);
            } finally {
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
