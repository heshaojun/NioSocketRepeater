package org.heath.runner;

import org.heath.service.AbstractLifeManager;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/15
 * @Description TODO
 */
public class ServerMsgHandler extends AbstractLifeManager {
    private ArrayBlockingQueue<byte[]> serverMsgQueue;


    public ServerMsgHandler(ArrayBlockingQueue<byte[]> serverMsgQueue) {
        this.serverMsgQueue = serverMsgQueue;
    }

    @Override
    public void boot() {

    }

    @Override
    public long getPeriod() {
        return Long.valueOf(System.getProperty("server.msg.handler.period", "20000"));
    }
}
