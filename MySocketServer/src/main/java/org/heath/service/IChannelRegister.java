package org.heath.service;

import java.nio.channels.SocketChannel;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public interface IChannelRegister {
    void registry(SocketChannel channel, int event);
}
