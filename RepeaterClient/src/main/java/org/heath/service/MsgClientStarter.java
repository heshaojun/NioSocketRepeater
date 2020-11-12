package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonStatus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
@Log4j2
public class MsgClientStarter {
    public static void startup() {
        log.info("启动消息客户端启动线程");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (CommonStatus.msgClientStatus != CommonStatus.MsgClientStatus.HEALTH) {
                    log.error("消息客户端状态异常，开始重置消息客户端生命状态");
                    if (CommonStatus.isMsgClientWorking) {
                        CommonStatus.isMsgClientAlive = false;
                        CommonStatus.isMsgClientWorking = false;
                    }
                    return;
                }
                if (!CommonStatus.isMsgClientAlive) {
                    log.info("消息客户端未启动，开始启动消息客户端");
                    new Thread(new MsgClient()).start();
                }
            }
        }, 100, 2 * 1000);
    }
}
