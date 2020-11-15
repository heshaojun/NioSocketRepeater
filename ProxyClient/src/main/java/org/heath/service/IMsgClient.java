package org.heath.service;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author shaojun he
 * @mail keepword_heshaojun@hotmail.com
 * @data 2020/11/15
 * 消息客户端接口
 */
public interface IMsgClient extends Runnable, IClient {
    boolean isWorking(); //判断但前客户端是否工作

    boolean isAlive();//判断当前客户端是否存活

    void startupWork();

    void startupLife();

    void stopWork();//停止工作

    void kill();//杀死当前客户端

    void startup();


    @Override
    default void run() {
        //通过定时器定时判断，实现自我管理
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isAlive()) {
                    startup();
                } else if (!isWorking()) {
                    kill();
                }
            }
        }, 100, 2 * 1000);
    }
}
