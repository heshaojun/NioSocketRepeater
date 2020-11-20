package org.heshaojun.runner;

import lombok.extern.log4j.Log4j2;
import org.heshaojun.common.CommonConst;
import org.heshaojun.service.AbstractAutoManager;
import org.heshaojun.service.AbstractServerMsgHandler;

/**
 * @author heshaojun
 * @date 2020/11/18
 * @description TODO
 */
@Log4j2
public class DefaultServerMsgHandler extends AbstractServerMsgHandler {
    public DefaultServerMsgHandler(AbstractAutoManager msgClientAutoManager) {
        super(msgClientAutoManager);
    }

    protected void handleDock(String id) {
        log.debug("接收到对接信息，开始进行对接" + id);
        try {
            CommonConst.DOCK_MSG_QUEUE.put(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void handleCMD(String cmd) {
        log.debug("接收到指令信息，开始执行指令" + cmd);
    }
}
