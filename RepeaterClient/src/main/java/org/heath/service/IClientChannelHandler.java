package org.heath.service;

import java.nio.channels.SelectionKey;

/**
 * @author shaojun he
 * @mail keepword_heshaojun@hotmail.com
 * @data 2020/11/08
 */
public interface IClientChannelHandler {
    public void handleRead(SelectionKey key);
}
