package org.heath.service;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description TODO
 */
public interface IMsgEvent {
    void attach(IMsgListener listener);

    void notifyAllLis();
}
