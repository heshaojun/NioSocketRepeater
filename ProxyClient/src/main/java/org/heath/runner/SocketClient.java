package org.heath.runner;

import org.heath.service.AbstractSocketClient;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/15
 * @Description 处理来自消息服务器的消息
 */
public class SocketClient extends AbstractSocketClient {
    private ArrayBlockingQueue<byte[]> serverMsgQueue;

    public SocketClient(ArrayBlockingQueue<byte[]> serverMsgQueue) {
        this.serverMsgQueue = serverMsgQueue;
    }

    @Override
    protected void handleMsg(byte[] data) {
        try {
            serverMsgQueue.put(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean auth(SocketChannel channel) {
        return true;
    }

    @Override
    public long getPeriod() {
        return Long.valueOf(System.getProperty("msg.client.period", "20000"));
    }
}
