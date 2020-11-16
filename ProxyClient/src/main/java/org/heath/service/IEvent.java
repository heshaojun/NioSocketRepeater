package org.heath.service;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description TODO
 */
public interface IEvent {
    void attach(IListener listener);

    void notifyAllLis();
}
