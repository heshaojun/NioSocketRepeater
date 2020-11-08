package org.heath.service;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/08
 * @Description TODO
 */
public abstract class AbstractClient implements IClient {
    private String ip;
    private int port;

    public AbstractClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public SocketChannel connect() {
        SocketChannel channel = null;
        try {
            channel = SocketChannel.open();
            channel.connect(new InetSocketAddress(ip, port));
            if (auth(channel)) {
                return channel;
            } else {
                channel.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (channel != null) channel.close();
            } catch (Exception e1) {
            }
        }
        return null;
    }

    public abstract boolean auth(SocketChannel channel);
}
