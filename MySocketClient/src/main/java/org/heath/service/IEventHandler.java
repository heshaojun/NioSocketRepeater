package org.heath.service;

import java.nio.channels.SelectionKey;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public interface IEventHandler {
    void handle(SelectionKey key);
}
