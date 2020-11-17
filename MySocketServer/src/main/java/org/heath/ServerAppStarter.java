package org.heath;

import org.heath.runner.MsgServerRunner;
import org.heath.service.MSgChannelRegister;
import org.heath.service.MsgReadHandler;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public class ServerAppStarter {
    public static void main(String[] args) {
        MsgReadHandler readHandler = new MsgReadHandler();
        MSgChannelRegister mSgChannelRegister = new MSgChannelRegister(readHandler);
        new MsgServerRunner(mSgChannelRegister).boot();
        new Thread(mSgChannelRegister).start();
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
            }
        }
    }
}
