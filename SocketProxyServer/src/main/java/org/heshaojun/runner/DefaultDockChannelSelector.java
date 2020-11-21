package org.heshaojun.runner;

import org.heshaojun.common.CommonProperties;
import org.heshaojun.service.AbstractChannelSelector;
import org.heshaojun.service.IEventHandler;

/**
 * @author heshaojun
 * @date 2020/11/20
 * @description TODO
 */
public class DefaultDockChannelSelector extends AbstractChannelSelector {
    public DefaultDockChannelSelector(IEventHandler eventHandler) {
        super(eventHandler);
    }

    @Override
    protected int getSelectorSize() {
        return CommonProperties.DOCK_REGISTER_SIZE;
    }
}
