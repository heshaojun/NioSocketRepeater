package org.heath.service;

import lombok.extern.log4j.Log4j2;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/17
 * @Description TODO
 */
@Log4j2
public abstract class AbstractMsgChannelSelector implements IChannelSelector, IRunner {
    private IEventHandler eventHandler;
    private List<Selector> selectors;

    public AbstractMsgChannelSelector(IEventHandler eventHandler) {
        this.eventHandler = eventHandler;
        int registerSize = Integer.valueOf(System.getProperty("msg.register.size", "1"));
        this.selectors = new ArrayList<>(registerSize);
        for (int i = 0; i < registerSize; ) {
            while (true) {
                try {
                    Selector selector = Selector.open();
                    selectors.add(selector);
                    break;
                } catch (Exception e) {
                }
            }
            i++;
        }
    }


    @Override
    public void boot() {
        log.info("开始启动所消息通道选择器线程");
        for (Selector selector : selectors) {
            new Thread(() -> {
                log.info("启动过选择器线程：" + Thread.currentThread().getName());
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
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void registry(SocketChannel channel, int event) {
        int location = 0;
        int index = 0;
        int minSize = Integer.MAX_VALUE;
        for (Selector selector : selectors) {
            int size = selector.keys().size();
            if (size > minSize) {
                location = index;
                minSize = size;
            }
            index++;
        }
        Selector selector = selectors.get(location);
        try {
            channel.register(selector, event);
            log.info("注册通道事件到消息选择器上成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("注册通道事件到消息选择器上失败");
        }
    }
}
