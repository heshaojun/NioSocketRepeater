package org.heath.service;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public interface IClientMsgProcessor {
    void handleMsg(byte[] data);
}
