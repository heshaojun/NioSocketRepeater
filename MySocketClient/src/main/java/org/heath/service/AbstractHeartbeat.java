package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
@Log4j2
public abstract class AbstractHeartbeat implements IRunner {
    @Override
    public void boot() {
        log.info("启动客户端心跳");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (CommonConst.CLIENT_MSG_QUEUE.remainingCapacity() < 1) return;
                    byte[] data = createHeartbeat();
                    CommonConst.CLIENT_MSG_QUEUE.put(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100, getPeriod());
    }

    protected abstract byte[] createHeartbeat();

    protected abstract long getPeriod();
}
