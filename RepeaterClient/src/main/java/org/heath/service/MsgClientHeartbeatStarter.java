package org.heath.service;

import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class MsgClientHeartbeatStarter {
    public void startup() {
        log.info("开始启动消息客户端心跳线程，定时生成心跳数据");
        Hashtable<String, String> msg = new Hashtable<>();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!CommonStatus.isIsMsgClientWorking) {
                    CommonConst.CLIENT_MSG_QUEUE.clear();
                    log.info("消息客户端未工作，暂时不生成心跳数据包");
                    CommonConst.CLIENT_MSG_QUEUE.clear();
                    return;
                }
                msg.put(CommonConst.TYPE, CommonConst.HEARTBEAT_TYPE);
                msg.put(CommonConst.HEARTBEAT, "" + new Date().getTime());
                byte[] heartbeat = MsgPackageUtils.packData(msg);
                try {
                    if (CommonConst.CLIENT_MSG_QUEUE.remainingCapacity() < 1) {
                        log.info("客户端消息队列已经已经占满，暂不存入心跳消息");
                        return;
                    }
                    CommonConst.CLIENT_MSG_QUEUE.put(heartbeat);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("存入新跳消息到队列异常", e);
                }
            }
        }, 500, 2 * 1000);
    }
}
