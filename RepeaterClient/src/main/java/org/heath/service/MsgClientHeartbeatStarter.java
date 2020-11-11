package org.heath.service;

import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;
import org.heath.common.CommonStatus;
import org.heath.utils.MsgPackageUtils;

import java.util.*;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
public class MsgClientHeartbeatStarter {
    public void startup() {
        Hashtable<String, String> msg = new Hashtable<>();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!CommonStatus.isIsMsgClientWorking) {
                    CommonConst.CLIENT_MSG_QUEUE.clear();
                    return;
                }
                msg.put(CommonConst.TYPE, CommonConst.HEARTBEAT_TYPE);
                msg.put(CommonConst.HEARTBEAT, "" + new Date().getTime());
                byte[] heartbeat = MsgPackageUtils.packData(msg);
                try {
                    CommonConst.CLIENT_MSG_QUEUE.put(heartbeat);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500, 2 * 1000);
    }
}
