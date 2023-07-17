package com.glaring.colourful.bully.fs.lib;


import android.content.Context;

import com.glaring.colourful.bully.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by wzh on 2020/7/3.
 * 用于随机生成AES密钥进行文件自加解密
 * 1.MD5(通过时间戳形成的MD5摘要,用于判断文件是否有修改，对于源文件有修改的情况不考虑)
 * 2.版本(用于后期升级,支持更好的自解密方案)
 * 3.KEY(AES自解密的密钥)
 * 4.长度(源文件的长度)
 * <p>
 * 当前版本主要用于AB包模式下的B包apk加解密
 */
public class encryption {
    //包含原文件长度,AES密钥,包的MD5
    private static String AES = "AES";
    private static final int VERSION = 0x80;
    private static final int KEY_SIZE = 16;
    private static final int MD5_SIZE = 16;

    private static final int HEADSIZE = MD5_SIZE + 1 + KEY_SIZE + 4;


    private static final void process(Cipher cipher, InputStream in, OutputStream out) throws IOException, BadPaddingException, IllegalBlockSizeException {
        byte[] buff = new byte[1024 * 4];
        while (true) {
            int mm = in.read(buff);
            if (mm < 0) {
                break;
            }
            byte[] data = cipher.update(buff, 0, mm);
            if (data != null) {
                out.write(data);
                out.flush();
            }
        }
        byte[] data = cipher.doFinal();
        if (data != null) {
            out.write(data);
            out.flush();
        }
    }

    private static boolean equals(byte[] source, byte[] target) {
        if (source == null) {//
            return false;
        }
        if (target == null || target.length == 0) {
            //用于校验的内容为空就不校验了
            return true;
        }
        if (source.length != target.length) {
            return false;
        }
        for (int i = 0; i < source.length; ++i) {
            if (source[i] != target[i]) {
                return false;
            }
        }
        return true;
    }

    private static int readInt(InputStream in) throws IOException {
        byte[] val = new byte[4];
        if (4 == in.read(val)) {
            return (((val[0] & 0xff) << 24) | ((val[1] & 0xff) << 16) | ((val[2] & 0xff) << 8) | ((val[3] & 0xff) << 0));
        } else {
            return -1;
        }
    }

    public static boolean decrypt(Context context, String resName, File target, byte[] md5Check) throws Exception {
        OutputStream out = null;
        InputStream in = null;
        SecretKey secretKey = null;

        try {
            if(false) {
                ZipFile apkfile = new ZipFile(context.getApplicationInfo().sourceDir);
                ZipEntry entry = apkfile.getEntry(resName);
                if (entry == null) {
                    return false;
                }
                in = new BufferedInputStream(apkfile.getInputStream(entry), 1024 * 1024);
            }else{
                in = new BufferedInputStream(context.getResources().openRawResource(R.raw.music));
            }
            byte[] md5 = new byte[MD5_SIZE];
            int version = 0;
            byte[] key = new byte[KEY_SIZE];
            int fileLength = 0;

            in.read(md5);
            //MD5校验，MD5相同就不进行下面的动作了
            if (equals(md5, md5Check)) {
                return false;
            }
            version = (in.read());
            if (version == VERSION) {
                in.read(key);
                fileLength = readInt(in);
            } else {
                throw new IOException("version error!" + version);
            }
            System.arraycopy(md5, 0, md5Check, 0, md5.length);
            try {
                secretKey = new SecretKeySpec(key, AES);
            } catch (Throwable e) {
                throw new IOException("密钥错误 or 文件未作加密处理");
            }
            if (target.exists()) {
                target.delete();
            }
            out = new BufferedOutputStream(new FileOutputStream(target), 1024 * 4);

            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            process(cipher, in, out);
        } finally {
            Close(in);
            Close(out);
        }
        return true;
    }

    public static void Close(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
