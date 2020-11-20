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
 * @Description 抽象socket 通道选择器，为通道提供注册入口结合通道注入的事件处理器处理选择器中就绪的事件通道，
 * 提供多路选择器同时并行提高通道数据处理的吞吐量
 */
@Log4j2
public abstract class AbstractChannelSelector implements IChannelSelector, IRunner {
    private IEventHandler eventHandler;
    private List<Selector> selectors;


    public AbstractChannelSelector(IEventHandler eventHandler) {
        this.eventHandler = eventHandler;
        int selectorSize = getSelectorSize();
        this.selectors = new ArrayList<>(selectorSize);
        for (int i = 0; i < selectorSize; ) {
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

    /**
     * 获取当前类型并行选股器轮循线程数量
     *
     * @return
     */
    protected abstract int getSelectorSize();

    @Override
    public void boot() {
        log.info("开始启动所消息通道选择器线程");
        for (Selector selector : selectors) {
            new Thread(() -> {
                log.info("启动过选择器线程：" + Thread.currentThread().getName());
                while (true) {
                    try {
                        if (selector.select() == 0) {
                            Thread.sleep(20);
                            continue;
                        }
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

    /**
     * 通道注册入口，暴露给使用者注册通道事件，实现采用最小注册量优先注册的方式
     *
     * @param channels 将要注册的通道
     * @param event    关注的事件
     */
    @Override
    public void registry(int event, SocketChannel... channels) {
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
            selector.wakeup();//唤醒阻塞选择器，避免过长阻塞
            for (SocketChannel channel : channels) {
                channel.register(selector, event);
            }
            log.info("注册通道事件到消息选择器上成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("注册通道事件到消息选择器上失败");
        }
    }
}
