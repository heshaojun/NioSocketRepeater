package org.heath.runner;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;
import org.heath.service.AbstractLifeManager;
import org.heath.service.AbstractSocketClient;
import org.heath.utils.MsgPackUtils;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/15
 * @Description TODO
 */
@Log4j2
public class ClientMsgHandler extends AbstractLifeManager {
    private ArrayBlockingQueue<byte[]> clientMsgQueue;
    private AbstractSocketClient msgSocketClient;
    private Timer heartbeatTimer;

    private Hashtable<String, String> heartbeatDataMap = new Hashtable<>();

    public ClientMsgHandler(ArrayBlockingQueue<byte[]> clientMsgQueue, AbstractSocketClient msgSocketClient) {
        this.clientMsgQueue = clientMsgQueue;
        this.msgSocketClient = msgSocketClient;
    }

    @Override
    public void run() {
        log.info("启动客户端消息处理线程");
        startup();
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.MSG_PACK_SIZE);
        try {
            clientMsgQueue.clear();
            heartbeatTimer = new Timer();
            workup();
            log.info("启动客户端心跳");
            heartbeat();
            while (isAlive()) {
                try {
                    byte[] clientMsgData = clientMsgQueue.poll(500, TimeUnit.MILLISECONDS);
                    if (!msgSocketClient.isWorking()) continue;
                    if (clientMsgData == null) continue;
                    if (!msgSocketClient.isAlive()) {
                        clientMsgQueue.clear();
                        continue;
                    }
                    if (!msgSocketClient.isWorking()) continue;
                    buffer.clear();
                    buffer.put(clientMsgData);
                    msgSocketClient.getChannel().write(buffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                heartbeatTimer.cancel();
            } catch (Exception e) {
            }
            try {
                clientMsgQueue.clear();
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
        return Long.valueOf(System.getProperty("client.msg.handler.period", "20000"));
    }

    private void heartbeat() {
        heartbeatTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    heartbeatDataMap.put(CommonConst.TYPE, CommonConst.HEARTBEAT_TYPE);
                    heartbeatDataMap.put(CommonConst.HEARTBEAT, "" + new Date().getTime());
                    byte[] data = MsgPackUtils.pack(heartbeatDataMap, CommonProperties.clientId, CommonProperties.MSG_PACK_SIZE);
                    clientMsgQueue.put(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100, Long.valueOf(System.getProperty("client.heartbeat.period", "5000")));
    }
}
