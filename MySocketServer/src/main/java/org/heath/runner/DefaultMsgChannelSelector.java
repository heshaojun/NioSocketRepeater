package org.heath.runner;

import org.heath.service.AbstractMsgChannelSelector;
import org.heath.service.IEventHandler;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/17
 * @Description TODO
 */
public class DefaultMsgChannelSelector extends AbstractMsgChannelSelector {
    public DefaultMsgChannelSelector(IEventHandler eventHandler) {
        super(eventHandler);
    }
}
