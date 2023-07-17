package com.glaring.colourful.bully.base;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public final class AES {
    private static final String AES = "AES";

    /**
     * default: keySize=128
     *
     * @return
     */
    public static SecretKey genKeyAES() throws Exception {
        return genKeyAES(128);
    }

    /**
     * @param keysize
     * @return
     */
    public static SecretKey genKeyAES(int keysize) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES);
        keyGen.init(keysize);
        return keyGen.generateKey();
    }

    public static String KeyToBase64(SecretKey key) {
        return BASE64.encode(key.getEncoded());
    }

    //将Base64编码后的AES秘钥转换成SecretKey对象
    public static SecretKey genKeyAES(String base64Key) throws Exception {
        return new SecretKeySpec(fullKeys(BASE64.decode(base64Key)), AES);
    }

    public static SecretKey genKeyAES(byte[] keys) throws Exception {
        return new SecretKeySpec(fullKeys(keys), AES);
    }

    public static byte[] fullKeys(byte[] keys) {
        final int N = keys == null ? 0 : keys.length;
        int M = 0;
        if (N == 0) {
            return null;
        } else if (N == 16 || N == 24 || N == 32) {
            return keys;
        } else if (N < 16) {
            M = 16;
        } else if (N < 24) {
            M = 24;
        } else if (N < 32) {
            M = 32;
        } else {
            return null;
        }
        final byte[] fullKeys = new byte[M];
        Arrays.fill(fullKeys, (byte) 0);
        System.arraycopy(keys, 0, fullKeys, 0, N);
        return fullKeys;
    }

    //加密
    public static byte[] encode(byte[] source, SecretKey key) throws Exception {
        return encode(source, key, AES);
    }

    //解密
    public static byte[] decode(byte[] source, SecretKey key) throws Exception {
        return decode(source, key, AES);
    }

    //加密
    public static byte[] encode(byte[] source, SecretKey key, String transformation) throws Exception {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(source);
    }

    //解密
    public static byte[] decode(byte[] source, SecretKey key, String transformation) throws Exception {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(source);
    }
}
