package org.heath.service;

import lombok.extern.log4j.Log4j2;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
@Log4j2
public class MSgChannelRegister implements IChannelRegister, Runnable {
    private IEventHandler eventHandler;
    private Selector selector;

    public MSgChannelRegister(IEventHandler eventHandler) {
        this.eventHandler = eventHandler;
        while (true) {
            try {
                selector = Selector.open();
                break;
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void registry(SocketChannel channel, int event) {
        try {
            channel.register(selector, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        log.info("消息通道事件注册器启动");
        while (true) {
            try {
                if (selector.select(500) == 0) continue;
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    eventHandler.handle(key);
                    keys.remove();
                }
            } catch (Exception e) {
            }

        }
    }
}
