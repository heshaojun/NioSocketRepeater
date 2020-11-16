package org.heath.common;

/**
 * @Author shaojun he
 * @Mail keepword_heshaojun@hotmail.com
 * @Date 2020/11/15
 * @Description TODO
 */
public class CommonProperties {

    public static final int MSG_PACK_SIZE = Integer.valueOf(System.getProperty("msg.size", "300"));
    public static final int MSG_PORT = Integer.valueOf(System.getProperty("msg.port", "8888"));
    public static final int FRONT_PORT = Integer.valueOf(System.getProperty("msg.port", "80"));
    public static final int DOCK_PORT = Integer.valueOf(System.getProperty("msg.port", "9999"));

    public static volatile String serverIdd = "default_server_id";
    public static volatile String clientId = "default_client_id";
}
