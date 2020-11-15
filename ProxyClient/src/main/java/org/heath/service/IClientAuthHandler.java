package org.heath.service;

import java.nio.channels.SocketChannel;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/15
 * @Description TODO
 */
public interface IClientAuthHandler {
    boolean auth(SocketChannel channel);
}
