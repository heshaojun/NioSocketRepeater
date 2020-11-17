package org.heath.service;

import org.heath.common.CommonProperties;

import java.nio.channels.Selector;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public class MsgChannelSelector extends AbstractChannelSelector {
    public MsgChannelSelector() {
        for (int i = 0; i < CommonProperties.MSG_SELECTOR_SIZE; ) {
            try {
                Selector selector = Selector.open();
                selectors.add(selector);
                i++;
            } catch (Exception e) {
            }
        }
    }
}
