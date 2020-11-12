package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonProperties;
import org.heath.common.CommonStatus;
import org.heath.utils.AESUtils;
import org.heath.utils.Base64Utils;
import org.heath.utils.CommonConst;
import org.heath.utils.MsgPackageUtils;
import sun.nio.ch.DirectBuffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.UUID;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/11
 * @Description TODO
 */
@Log4j2
public class MsgServer extends AbstractServer {

    @Override
    public void run() {
        log.info("开始启动消息服务端");
        if (!CommonStatus.isMsgServerAlive) {
            CommonStatus.isMsgServerAlive = true;
            startup();
            log.info("消息服务端停止运行");
            CommonStatus.isMsgServerWorking = false;
            CommonStatus.isMsgServerAlive = false;
        }
    }

    @Override
    protected ServerSocketChannel createServer() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(CommonProperties.MSG_PORT), 10);
        serverSocketChannel.configureBlocking(false);
        CommonStatus.isMsgServerWorking = true;
        return serverSocketChannel;
    }

    @Override
    protected void handleAccept(SelectionKey key) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel channel = serverSocketChannel.accept();
            channel.configureBlocking(false);
            if (!verify(channel)) {
                channel.close();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void handleRead(SelectionKey key) {

    }

    private boolean verify(SocketChannel channel) {
        boolean result = false;
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.AUTH_PACKAGE_SIZE);
        try {
            log.info("向消息客户端写入认证数据");
            String token = UUID.randomUUID().toString().replaceAll("-", "");
            String serverId = UUID.randomUUID().toString().replaceAll("-", "");
            byte[] serverAESKeyBytes = AESUtils.generateKey();
            String serverAESKey = Base64Utils.encodeToString(serverAESKeyBytes);
            Hashtable<String, String> authDataMap = new Hashtable<>();
            authDataMap.put(CommonConst.TOKEN, token);
            authDataMap.put(CommonConst.KEY, serverAESKey);
            byte[] dataBytes = MsgPackageUtils.packAuthData(authDataMap, serverId);

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
}
