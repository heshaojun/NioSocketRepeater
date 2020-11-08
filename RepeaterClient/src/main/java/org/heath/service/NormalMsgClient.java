package org.heath.service;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/08
 * @Description TODO
 */
public class NormalMsgClient extends AbstractMsgClient {


    public NormalMsgClient(String ip, int port, int dataPackageSize, IClientChannelHandler msClientChannelHandler, ArrayBlockingQueue<byte[]> ackMsgQueue) {
        super(ip, port, dataPackageSize, msClientChannelHandler, ackMsgQueue);
    }

    @Override
    public boolean auth(SocketChannel channel) {
        return false;
    }
}
