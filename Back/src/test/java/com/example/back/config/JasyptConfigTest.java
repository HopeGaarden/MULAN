package com.example.back.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static com.example.back.config.IntegrationHelper.NON_ASCII;

@SuppressWarnings(NON_ASCII)
class JasyptConfigTest extends IntegrationHelper {
    @Value("${jasypt.encryptor.password}")
    private String key;

    @Test
    void 암호화() {
        String value = "test";
        System.out.println(jasyptEncoding(value));
    }

    @Test
    void 복호화() {
        String value = "nsHveCItHp8U6rPPdTWfUA==";
        BasicTextEncryptor enc = new BasicTextEncryptor();
        enc.setPassword(key);
        System.out.println(enc.decrypt(value));
    }

    public String jasyptEncoding(String value) {
        StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
        enc.setAlgorithm("PBEWithMD5AndDES");
        enc.setPassword(key);
        return enc.encrypt(value);
    }
}