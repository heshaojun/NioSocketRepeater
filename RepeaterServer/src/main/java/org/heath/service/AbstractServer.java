package org.heath.service;

import java.nio.channels.ServerSocketChannel;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/08
 * @Description TODO
 */
public abstract class AbstractServer implements IServer, ILifeMonitor {
    private int port;
    private int logback;
    private IServerChannelHandler serverChannelHandler;
    private volatile boolean isAlive = false;
    private volatile boolean isWorking = false;

    public AbstractServer(int port, int logback, IServerChannelHandler serverChannelHandler) {
        this.port = port;
        this.logback = logback;
        this.serverChannelHandler = serverChannelHandler;
    }

    private void workup() {
        isAlive = true;
        ServerSocketChannel serverSocketChannel = null;

    }

}
