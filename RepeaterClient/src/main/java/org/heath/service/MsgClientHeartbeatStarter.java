package org.heath.service;

import org.heath.common.CommonProperties;
import org.heath.common.CommonStatus;
import org.heath.utils.MsgPackageUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
public class MsgClientHeartbeatStarter {
    public void startup() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!CommonStatus.isMsgClientAlive) return;
                byte[] heartbeat = MsgPackageUtils.generateHeartbeat();
                try {
                    CommonProperties.CLIENT_MSG_QUEUE.put(heartbeat);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000, 10 * 1000);
    }
}
