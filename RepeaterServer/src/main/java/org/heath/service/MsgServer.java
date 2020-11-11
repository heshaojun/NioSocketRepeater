package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonStatus;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/11
 * @Description TODO
 */
@Log4j2
public class MsgServer implements IServer {
    @Override
    public void startup() {
        CommonStatus.isMsgServerAlive = true;
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (!CommonStatus.isMsgServerAlive) startup();
    }
}
