package org.heath.service;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description 消息服务器消息处理单元接口
 */
public interface IServerProcessor extends IEvent {
    void handle(byte[] data);
}
