package org.heath.runner;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonProperties;
import org.heath.service.AbstractLifeManager;
import org.heath.service.IServerMsgProcessor;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/15
 * @Description TODO
 */
@Log4j2
public class ServerMsgHandler extends AbstractLifeManager {
    private ArrayBlockingQueue<byte[]> serverMsgQueue;
    private IServerMsgProcessor serverMsgProcessor;

    public ServerMsgHandler(ArrayBlockingQueue<byte[]> serverMsgQueue, IServerMsgProcessor serverMsgProcessor) {
        this.serverMsgQueue = serverMsgQueue;
        this.serverMsgProcessor = serverMsgProcessor;
    }

    @Override
    public void run() {
        log.info("启动服务端的消息处理线程");
        startup();
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.MSG_PACK_SIZE);
        workup();
        try {
            try {
                while (isAlive()) {
                    byte[] data = serverMsgQueue.poll(500, TimeUnit.MILLISECONDS);
                    if (data == null) continue;
                    serverMsgProcessor.handle(data);
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
        } finally {
            try {
                serverMsgQueue.clear();
            } catch (Exception e) {
            }
            shutdown();
            try {
                ((DirectBuffer) buffer).cleaner().clean();
            } catch (Exception e) {
            }
        }
    }


    @Override
    public long getPeriod() {
        return Long.valueOf(System.getProperty("server.msg.handler.period", "20000"));
    }
}
