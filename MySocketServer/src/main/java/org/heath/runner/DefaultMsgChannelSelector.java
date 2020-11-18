package org.heath.runner;

import org.heath.service.AbstractChannelSelector;
import org.heath.service.IEventHandler;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/17
 * @Description TODO
 */
public class DefaultMsgChannelSelector extends AbstractChannelSelector {
    public DefaultMsgChannelSelector(IEventHandler eventHandler) {
        super(eventHandler);
    }

    @Override
    protected int getSelectorSize() {
        return Integer.valueOf(System.getProperty("msg.register.size", "1"));
    }
}
