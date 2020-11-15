package org.heath.service;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/15
 * @Description TODO
 */
public class ClientMsgHandler implements IClientMsgHandler {
    private ArrayBlockingQueue<byte[]> clientMsgQueue;
    @Override
    public void handle(SocketChannel channel) {

    }
}
