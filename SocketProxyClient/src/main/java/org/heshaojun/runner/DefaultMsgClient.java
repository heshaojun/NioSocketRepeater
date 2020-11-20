package org.heshaojun.runner;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.heshaojun.common.CommonConst;
import org.heshaojun.common.CommonProperties;
import org.heshaojun.service.AbstractMsgClient;
import org.heshaojun.utils.AESUtils;
import org.heshaojun.utils.MsgPackUtils;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
@Log4j2
public class DefaultMsgClient extends AbstractMsgClient {

    @Override
    protected boolean auth(SocketChannel channel) {
        boolean result = false;
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.AUTH_PACK_SIZE);
        try {
            while (true) {
                if (channel.read(buffer) == -1) throw new Exception("读取到结尾");
                if (buffer.position() == buffer.limit()) break;
                Thread.sleep(10);
            }
            byte[] data = new byte[buffer.limit()];
            buffer.flip();
            buffer.get(data);
            Map<String, String> map = MsgPackUtils.unpackAuth(data, CommonProperties.RSA_KEY);
            log.info("接收到的认证消息为：" + map);
            if (map == null) throw new Exception("非法认证数据");
            byte[] serverKeyBytes = Base64.decodeBase64(map.get(CommonConst.KEY));
            byte[] clientKeyBytes = AESUtils.generateKey();
            map.put(CommonConst.KEY, Base64.encodeBase64String(clientKeyBytes));
            data = MsgPackUtils.packAuth(map, CommonProperties.AUTH_PACK_SIZE, CommonProperties.RSA_KEY);
            buffer.clear();
            buffer.put(data);
            buffer.flip();
            while (true) {
                channel.write(buffer);
                if (buffer.position() == buffer.limit()) break;
                Thread.sleep(10);
            }
            CommonProperties.token = map.get(CommonConst.TOKEN);
            CommonProperties.authId = map.get(CommonConst.AUTH_ID);
            CommonProperties.serverKey = serverKeyBytes;
            CommonProperties.clientKey = clientKeyBytes;
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            try {
                channel.close();
            } catch (Exception e1) {
            }
        } finally {
            try {
                ((DirectBuffer) buffer).cleaner().clean();
            } catch (Exception e) {
            }
        }
        return result;
    }
}
