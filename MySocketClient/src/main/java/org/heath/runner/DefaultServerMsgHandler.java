package org.heath.runner;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonConst;
import org.heath.service.AbstractAutoManager;
import org.heath.service.AbstractServerMsgHandler;
import org.heath.service.IChannelSelector;

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
    }

    protected void handleCMD(String cmd) {
        log.debug("接收到指令信息，开始执行指令" + cmd);
    }
}
