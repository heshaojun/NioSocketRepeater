package org.heath;

import org.heath.service.DefaultClientBuilder;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/15
 * @Description TODO
 */
public class ClientAppStarter {
    public static void main(String[] args) {
        DefaultClientBuilder builder = DefaultClientBuilder.create();
        builder.build().startup();
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
            }
        }
    }
}
