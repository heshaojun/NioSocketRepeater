package org.heath.service;

import java.nio.channels.SocketChannel;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public interface IChannelSelector {
    void registry(int event, SocketChannel... channels);
}
