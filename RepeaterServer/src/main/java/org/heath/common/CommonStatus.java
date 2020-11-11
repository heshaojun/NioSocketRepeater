package org.heath.common;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
public class CommonStatus {
    public static volatile boolean isMsgServerAlive = false;//消息服务端存活状态
    public static volatile boolean isMsgServerWorking = false;//消息服务端工作状态

    public static volatile boolean isDockServerAlive = false;//对接服务端存活状态
    public static volatile boolean isDockServerWorking = false;//对接服务端工作状态

    public static volatile boolean isFrontServerAlive = false;//前置服务端存活状态
    public static volatile boolean isFrontServerWorking = false;//前置服务端工作状态

}
