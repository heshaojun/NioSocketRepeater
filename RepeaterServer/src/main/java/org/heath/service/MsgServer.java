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
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
        log.info("启动消息服务器，绑定端口为：" + CommonProperties.MSG_PORT);
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
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            CommonProperties.ClientInfoMap clientMapInfo = CommonProperties.MSG_CLIENT_INFO_MAP.get(channel);
            ByteBuffer buffer = clientMapInfo.getBuffer();
            if (channel.read(buffer) == -1) throw new IOException("读取到结尾");
            if (buffer.position() == buffer.limit()) {
                buffer.flip();
                byte[] data = new byte[buffer.limit()];
                buffer.get(data);
                buffer.clear();
                CompletableFuture.runAsync(() -> {
                    handleMsg(data, clientMapInfo);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e1) {
                }
                try {
                    CommonProperties.MSG_CLIENT_INFO_MAP.remove(channel);
                } catch (Exception e1) {
                }
            }
        }
    }

    private void handleMsg(byte[] data, CommonProperties.ClientInfoMap clientMapInfo) {
        try {
            Hashtable<String, String> map = MsgPackageUtils.unpackData(data, clientMapInfo);
            if (map == null) throw new IOException("数据包异常");
            log.info("读取到的客户端消息为：" + map);
        } catch (Exception e) {
            e.printStackTrace();
            SocketChannel channel = clientMapInfo.getChannel();
            try {
                channel.close();
            } catch (Exception e1) {
            }
            try {
                CommonProperties.MSG_CLIENT_INFO_MAP.remove(channel);
            } catch (Exception e1) {
            }
        }

    }

    private boolean verify(SocketChannel channel) {
        boolean result = false;
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.AUTH_PACKAGE_SIZE);
        try {
            log.info("向消息客户端写入认证数据");
            String token = UUID.randomUUID().toString().replaceAll("-", "");
            String serverId = UUID.randomUUID().toString().replaceAll("-", "");
            byte[] serverAESKey = AESUtils.generateKey();
            String serverKey = Base64Utils.encodeToString(serverAESKey);
            Hashtable<String, String> authDataMap = new Hashtable<>();
            authDataMap.put(CommonConst.TOKEN, token);
            authDataMap.put(CommonConst.KEY, serverKey);
            byte[] dataBytes = MsgPackageUtils.packAuthData(authDataMap, serverId);
            buffer.put(dataBytes);
            buffer.flip();
            log.info("向消息客户端写入认证数据");
            for (int i = 0; i < 10000; i++) {
                channel.write(buffer);
                if (buffer.position() == buffer.limit()) break;
                Thread.sleep(10);
            }
            if (buffer.position() != buffer.limit()) throw new IOException("写入到数据不完整");
            //读取token
            log.info("开始读取消息客户端到验证token");
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
            String readToken = dataMap.get(CommonConst.TOKEN);
            String clientId = dataMap.get(CommonConst.CLIENT_ID);
            String clientKey = dataMap.get(CommonConst.KEY);
            byte[] clientAESKey = Base64Utils.decode(clientKey);
            log.info("解析读取到消息服务器到数据 readToken:" + readToken + "  clientId:" + clientId + "  clientKey:" + clientKey);
            if (readToken == null || "".equals(readToken) || clientId == null || "".equals(clientId) || clientKey == null || "".equals(clientKey)) {
                log.info("认证数据不合法");
                return false;
            }
            if (!readToken.equals(token)) {
                log.info("认证token不相同");
            }
            CommonProperties.MSG_CLIENT_INFO_MAP.put(channel, new CommonProperties.ClientInfoMap(serverId, clientId, serverAESKey, clientAESKey, channel));
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
