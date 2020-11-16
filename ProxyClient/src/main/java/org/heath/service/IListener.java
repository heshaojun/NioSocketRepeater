package org.heath.service;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description TODO
 */
public interface IListener {
    default void listen(IEvent... events) {
        for (IEvent event : events) {
            event.attach(this);
        }
    }

    void update();
}
