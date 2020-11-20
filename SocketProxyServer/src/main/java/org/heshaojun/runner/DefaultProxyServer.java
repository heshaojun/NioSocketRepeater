package org.heshaojun.runner;

import lombok.extern.log4j.Log4j2;
import org.heshaojun.common.CommonConst;
import org.heshaojun.service.AbstractProxyServer;

import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * @author heshaojun
 * @date 2020/11/20
 * @description 默认前置代理服务器
 */
@Log4j2
public class DefaultProxyServer extends AbstractProxyServer {
    @Override
    protected void handleAccept(SelectionKey key) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = null;
        try {
            channel = serverSocketChannel.accept();
            channel.configureBlocking(false);
            if (CommonConst.MSG_CLIENT_INFO_MAP.isEmpty()) {
                log.warn("没有可用的消息客户端连接");
                channel.close();
                return;
            }
            String id = UUID.randomUUID().toString();
            log.info("有新的连接，连接id为：" + id);
            CommonConst.CACHED_CHANNEL_INFO_MAP.put(id, new CommonConst.CachedChannelInfo(channel, id));
            CommonConst.DOCK_MSG_QUEUE.put(id);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (channel != null) channel.close();
            } catch (Exception e1) {
            }
        }
    }
}
