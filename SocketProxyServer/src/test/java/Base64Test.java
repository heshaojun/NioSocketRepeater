import org.apache.commons.codec.binary.Base64;
import org.heshaojun.utils.CertificateUtils;

import java.util.UUID;

/**
 * @author heshaojun
 * @date 2020/11/23
 * @description TODO
 */
public class Base64Test {
    public static void main(String[] args) {
        String passWord = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        System.out.println(passWord);
        CertificateUtils.generateCer("rsa", passWord);
    }
}
