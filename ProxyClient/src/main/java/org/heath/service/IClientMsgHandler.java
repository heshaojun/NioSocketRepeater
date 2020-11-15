package org.heath.service;

import java.nio.channels.SocketChannel;

/**
 * @author shaojun he
 * @mail keepword_heshaojun@hotmail.com
 * @data 2020/11/15
 */
public interface IClientMsgHandler {
    void handle(SocketChannel channel);
}
