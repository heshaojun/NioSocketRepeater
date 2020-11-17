package org.heath.service;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description 自我生命管理，实现自检自启动
 */
public interface ILifeManager extends Runnable {
    boolean isWorking(); //判断但前客户端是否工作

    boolean isAlive();//判断当前客户端是否存活

    void workup();//启动工作

    void hangup();//停止工作

    void shutdown();//杀死当前客户端

    void startup();//启动

    long getPeriod();//获取线程自检周期

    default void boot() {
        //通过定时器定时判断，实现自我管理
        final ILifeManager lifeManager = this;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isAlive()) {
                    try {
                        new Thread(lifeManager).start();
                    } catch (Exception e) {
                    }
                }
            }
        }, 100, getPeriod());
    }

}
