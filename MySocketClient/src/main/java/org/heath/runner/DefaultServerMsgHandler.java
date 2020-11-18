package org.heath.runner;

import org.heath.common.CommonConst;
import org.heath.service.AbstractAutoManager;
import org.heath.service.AbstractServerMsgHandler;
import org.heath.service.IChannelSelector;

/**
 * @author heshaojun
 * @date 2020/11/18
 * @description TODO
 */
public class DefaultServerMsgHandler extends AbstractServerMsgHandler {
    public DefaultServerMsgHandler(IChannelSelector channelSelector, AbstractAutoManager msgClientAutoManager) {
        super(channelSelector, msgClientAutoManager);
    }


    protected void handleDock(String id) {

    }

    protected void handleCMD(String cmd) {
    }
}
