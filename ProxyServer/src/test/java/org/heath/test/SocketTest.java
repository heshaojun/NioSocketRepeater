package org.heath.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public class SocketTest {
    public static void main(String[] args) throws Exception {
        String dataStr = "header@default_client_id@dHlwZTpoZWFydGJlYXRfdHlwZUBoZWFydGJlYXQ6MTYwNTU4NDkxNDQzN0A=@###################################################################################################################";
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 8888));
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(dataStr.getBytes());
        outputStream.flush();
        outputStream.close();
        Thread.sleep(1000);
        System.out.println("发送数据");

    }
}
