package org.heshaojun.runner;

import org.heshaojun.common.CommonProperties;
import org.heshaojun.service.AbstractChannelSelector;
import org.heshaojun.service.IEventHandler;

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
        return CommonProperties.MSG_REGISTER_SIZE;
    }
}
