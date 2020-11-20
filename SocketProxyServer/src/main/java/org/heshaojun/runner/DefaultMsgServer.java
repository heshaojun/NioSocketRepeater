package org.heshaojun.runner;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.heshaojun.common.CommonConst;
import org.heshaojun.common.CommonProperties;
import org.heshaojun.service.AbstractMsgServer;
import org.heshaojun.service.IChannelSelector;
import org.heshaojun.utils.AESUtils;
import org.heshaojun.utils.MsgPackUtils;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
@Log4j2
public class DefaultMsgServer extends AbstractMsgServer {

    public DefaultMsgServer(IChannelSelector channelRegister) {
        super(channelRegister);
    }

    @Override
    protected boolean auth(SocketChannel channel) {
        boolean result = false;
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.AUTH_PACK_SIZE);
        try {
            log.info("开对客户端：" + channel.getRemoteAddress() + " 进行认证");
            String authId = UUID.randomUUID().toString().replaceAll("-", "");
            String token = UUID.randomUUID().toString().replaceAll("-", "") + "_" + (new Date().getTime());
            String serverKey = Base64.encodeBase64String(AESUtils.generateKey());
            Map<String, String> map = new Hashtable<>();
            map.put(CommonConst.TYPE, CommonConst.TOKEN_TYPE);
            map.put(CommonConst.TOKEN, token);
            map.put(CommonConst.AUTH_ID, authId);
            map.put(CommonConst.KEY, serverKey);
            log.info("认证数据为：" + map);
            byte[] data = MsgPackUtils.packAuth(map, CommonProperties.AUTH_PACK_SIZE, CommonProperties.RSA_KEY);
            buffer.clear();
            buffer.put(data);
            buffer.flip();
            log.info("写入认证数据");
            while (true) {
                channel.write(buffer);
                if (buffer.position() == buffer.limit()) break;
                Thread.sleep(10);
            }
            log.info("读取客户端的认证数据");
            buffer.clear();
            for (int i = 0; i < 1000; i++) {
                if (channel.read(buffer) == -1) throw new Exception("读取带结尾");
                if (buffer.position() == buffer.limit()) break;
                Thread.sleep(30);
            }
            if (buffer.position() != buffer.limit()) throw new Exception("没有接收到完整的数据包");
            buffer.flip();
            data = new byte[buffer.limit()];
            buffer.get(data);
            map = MsgPackUtils.unpackAuth(data, CommonProperties.RSA_KEY);
            log.info("读取到的认证数据为：" + map);
            if (map == null) throw new Exception("非法认证数据");
            if (token.equals(map.get(CommonConst.TOKEN)) && authId.equals(map.get(CommonConst.AUTH_ID)) && !"".equals(map.get(CommonConst.KEY))) {
                result = true;
                log.info("客户端" + channel.getRemoteAddress() + " 通过认证");
                CommonConst.MSG_CLIENT_INFO_MAP.put(channel, new CommonConst.MsgClientInfo(channel, authId, Base64.decodeBase64(serverKey), Base64.decodeBase64(map.get(CommonConst.KEY))));
            } else {
                result = false;
                log.info("客户端没有通过认证");
            }
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
