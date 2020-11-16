package org.heath.service;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description TODO
 */
public interface IMsgListener {
    default void listen(IMsgEvent... events) {
        for (IMsgEvent event : events) {
            event.attach(this);
        }
    }

    void update();
}
