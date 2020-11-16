package org.heath.service;

import java.nio.channels.SelectionKey;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description TODO
 */
public interface ISocketServer extends ILifeManager {
    void handleAccept(SelectionKey key);
}
