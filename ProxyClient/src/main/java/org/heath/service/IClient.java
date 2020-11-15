package org.heath.service;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author shaojun he
 * @mail keepword_heshaojun@hotmail.com
 * @data 2020/11/15
 */
public interface IClient {
    default SocketChannel connect(String ip, int port) {
        SocketChannel channel = null;
        try {
            channel = SocketChannel.open();
            channel.connect(new InetSocketAddress(ip, port));
            channel.configureBlocking(false);
        } catch (Exception e) {
            e.printStackTrace();
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e1) {
                }
                channel = null;
            }
        }
        return channel;
    }
}
