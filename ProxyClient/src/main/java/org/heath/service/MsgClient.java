package org.heath.service;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/15
 * @Description TODO
 */
public class MsgClient extends AbstractMsgClient {
    private ArrayBlockingQueue<byte[]> serverMsgQueue;

    public MsgClient(IClientAuthHandler authHandler, IClientMsgHandler clientMsgHandler, ArrayBlockingQueue<byte[]> serverMsgQueue) {
        super(authHandler, clientMsgHandler);
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
}
