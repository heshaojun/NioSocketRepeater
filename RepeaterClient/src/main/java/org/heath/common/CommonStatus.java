package org.heath.common;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
public class CommonStatus {
    public static volatile MsgClientStatus msgClientStatus = MsgClientStatus.HEALTH; //消息客户端状态
    public static volatile boolean isMsgClientAlive = false;//消息客户端存活状态
    public static volatile boolean isIsMsgClientWorking = false;
    public static volatile boolean isRepeaterAlive = false;//中继器线程存活状态


    public static enum MsgClientStatus {
        HEALTH, //健康
        BAD_DATA,//数据错误
        BREAK_CHANNEL//中断的通道
    }
}
