package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonStatus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author heshaojun
 * @date 2020/11/12
 * @description TODO
 */
@Log4j2
public class MsgServerStarter {
    public static void startup() {
        log.info("启动消息服务端启动线程");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!CommonStatus.isMsgServerAlive) {
                    log.info("消息服务端未启动，开始启动消息服务端");
                    new Thread(new MsgServer()).start();
                } else if (!CommonStatus.isMsgServerWorking) {
                    log.info("消息服务器未工作，开始关闭消息服务器");
                    CommonStatus.isMsgServerAlive = false;
                    return;
                }

            }
        }, 100, 2 * 1000);
    }
}
