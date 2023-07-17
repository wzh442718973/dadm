package com.glaring.colourful.bully.base;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class MD5 {

    public static final byte[] encode(byte[] in) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(in);
            return digest.digest();
        } catch (Throwable e) {
            return null;
        }
    }

    public static String encode(String in) {
        return HEX.toHex(encode(in.getBytes()));
    }

    public static String encode(File infile) throws Exception {
        FileInputStream fIn = null;
        byte[] buff = new byte[1024];
        try {
            fIn = new FileInputStream(infile);
            MessageDigest digest = MessageDigest.getInstance("MD5");
            do {
                int rs = fIn.read(buff);
                if (rs < 0) {
                    break;
                } else if (rs > 0) {
                    digest.update(buff, 0, rs);
                }
            } while (true);
            return HEX.toHex(digest.digest());
        } finally {
            IO.Close(fIn);
        }
    }
}
