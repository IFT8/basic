package me.ift8.basic.utils;

import me.ift8.basic.encryption.Encryption;
import org.junit.Test;

/**
 * @author biezhi
 * @date 2018/8/21
 */
public class EncryptionTest {

    private Encryption encryption = new Encryption();

    @Test
    public void testEncrypt() throws Exception {
        String encrypt = encryption.encrypt("你好世界,Hello world");
        System.out.println("【"+encrypt+"】");
    }

    @Test
    public void testDecrypt() throws Exception {
        String encrypt = encryption.decrypt("gJTg/2c6H9OqSV+lpD9/DbugCUco7E+xI+PdCD7ZMX0=");
        System.out.println("【"+encrypt+"】");
    }

}
