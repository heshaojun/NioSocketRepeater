package org.heath.service;

import org.heath.common.CommonStatus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
public class MsgClientStarter {
    public void startup() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (CommonStatus.msgClientStatus != CommonStatus.MsgClientStatus.HEALTH) {
                    CommonStatus.isMsgClientAlive = false;
                    return;
                }
                if (!CommonStatus.isMsgClientAlive) {
                    new Thread(new MsgClient()).start();
                }
            }
        }, 100, 2000);
    }
}
