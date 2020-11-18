package org.heath.common;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public class CommonProperties {
    public static final int PACK_SIZE = Integer.valueOf(System.getProperty("pack.size", "200"));
    public static volatile String authId = "authId";
    public static volatile String token = "token";
}
