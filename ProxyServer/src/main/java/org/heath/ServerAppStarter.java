package org.heath;

import org.heath.service.SocketServers;
import org.heath.service.SocketServersBuilder;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/15
 * @Description TODO
 */
public class ServerAppStarter {
    public static void main(String[] args) {
        SocketServersBuilder builder = SocketServersBuilder.create();
        SocketServers socketServers = builder.build();
        socketServers.startup();
        while (true) {
            try {
                Thread.sleep(1000000);
            } catch (Exception e) {
            }
        }
    }
}
